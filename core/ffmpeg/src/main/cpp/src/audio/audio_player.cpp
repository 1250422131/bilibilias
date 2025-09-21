#include "audio/audio_player.hpp"
#include "jni_log.hpp"

extern "C" {
#include "libavutil/frame.h"
#include "libavutil/channel_layout.h"
}

namespace bilias {

    constexpr int kDefaultSampleRate = 48000;
    constexpr oboe::AudioFormat kDefaultAudioFormat = oboe::AudioFormat::Float; // For Oboe
    constexpr AVSampleFormat kDefaultAVSampleFormat = AV_SAMPLE_FMT_FLT;       // For FFmpeg
    constexpr int kDefaultChannels = 2;

    AudioPlayer::AudioPlayer(ThreadSafeQueue<AVFrame *> &frame_queue)
            : audio_frame_queue_(frame_queue) {}

    AudioPlayer::~AudioPlayer() {
        stop();
    }

    bool AudioPlayer::start(AudioParams params) {
        if (is_playing_.load()) {
            return true;
        }
        audio_params_ = params;
        if (!init_resampler()) {
            log_e("Failed to initialize resampler");
            return false;
        }

        oboe::AudioStreamBuilder builder;
        builder.setDirection(oboe::Direction::Output)
                ->setPerformanceMode(oboe::PerformanceMode::LowLatency)
                ->setSharingMode(oboe::SharingMode::Shared)
                ->setFormat(kDefaultAudioFormat)
                ->setChannelCount(kDefaultChannels)
                ->setSampleRate(kDefaultSampleRate)
                ->setDataCallback(this);

        oboe::Result result = builder.openStream(stream_);
        if (result != oboe::Result::OK) {
            log_e("Failed to open Oboe stream: %s", oboe::convertToText(result));
            return false;
        }

        result = stream_->requestStart();
        if (result != oboe::Result::OK) {
            log_e("Failed to start Oboe stream: %s", oboe::convertToText(result));
            return false;
        }

        is_playing_.store(true);
        log_i("Oboe stream started successfully");
        return true;
    }

    void AudioPlayer::stop() {
        if (!is_playing_.load()) {
            return;
        }
        is_playing_.store(false);

        if (stream_) {
            stream_->stop();
            stream_->close();
            stream_.reset();
        }
        destroy_resampler();

        if (current_frame_) {
            av_frame_free(&current_frame_);
            current_frame_ = nullptr;
        }
        log_i("Oboe stream stopped");
    }

    oboe::DataCallbackResult AudioPlayer::onAudioReady(
            oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) {

        if (!is_playing_.load()) {
            // Silence the buffer if not playing
            memset(audioData, 0, numFrames * oboeStream->getBytesPerFrame());
            return oboe::DataCallbackResult::Continue;
        }

        auto *output_buffer = static_cast<float *>(audioData);
        int frames_filled = 0;

        while (frames_filled < numFrames) {
            if (!current_frame_) {
                if (!audio_frame_queue_.try_pop(current_frame_)) {
                    // Queue is empty, fill remaining with silence
                    memset(output_buffer + frames_filled, 0,
                           (numFrames - frames_filled) * oboeStream->getBytesPerFrame());
                    return oboe::DataCallbackResult::Continue;
                }
                current_frame_offset_ = 0;
                // Update clock
                if (current_frame_->pts != AV_NOPTS_VALUE) {
                    clock_ = current_frame_->pts * av_q2d(audio_params_.time_base);
                }
            }

            int remaining_samples = current_frame_->nb_samples - current_frame_offset_;
            int64_t delay = swr_get_delay(swr_ctx_, audio_params_.sample_rate);
            int64_t dst_nb_samples__ = av_rescale_rnd(delay + remaining_samples, kDefaultSampleRate, audio_params_.sample_rate, AV_ROUND_UP);
            int dst_nb_samples = std::min((int64_t) (numFrames - frames_filled), dst_nb_samples__);


            uint8_t *out_buffer_ptr = resample_buffer_;
            uint8_t *in_data[AV_NUM_DATA_POINTERS];
            memcpy(in_data, current_frame_->data, sizeof(current_frame_->data));

            int bytes_per_sample = av_get_bytes_per_sample(audio_params_.sample_format);
            if (av_sample_fmt_is_planar(audio_params_.sample_format)) {
                for (int i = 0; i < audio_params_.channel_layout.nb_channels; i++) {
                    in_data[i] += current_frame_offset_ * bytes_per_sample;
                }
            } else {
                in_data[0] += current_frame_offset_ * bytes_per_sample * audio_params_.channel_layout.nb_channels;
            }


            int converted_samples = swr_convert(
                    swr_ctx_,
                    &out_buffer_ptr,
                    dst_nb_samples,
                    (const uint8_t **) in_data,
                    remaining_samples
            );

            if (converted_samples < 0) {
                log_e("swr_convert failed");
                av_frame_free(&current_frame_);
                current_frame_ = nullptr;
                continue;
            }

            int consumed_samples = av_rescale_rnd(converted_samples, audio_params_.sample_rate, kDefaultSampleRate, AV_ROUND_UP);

            memcpy(output_buffer + frames_filled, resample_buffer_,
                   converted_samples * oboeStream->getBytesPerFrame());


            frames_filled += converted_samples;
            current_frame_offset_ += consumed_samples;

            if (current_frame_offset_ >= current_frame_->nb_samples) {
                av_frame_free(&current_frame_);
                current_frame_ = nullptr;
                current_frame_offset_ = 0;
            }
        }

        if (frames_filled < numFrames) {
            memset(output_buffer + frames_filled, 0,
                   (numFrames - frames_filled) * oboeStream->getBytesPerFrame());
        }

        return oboe::DataCallbackResult::Continue;
    }

    bool AudioPlayer::init_resampler() {
        destroy_resampler();
    
        swr_alloc_set_opts2(&swr_ctx_,
                            &audio_params_.channel_layout, kDefaultAVSampleFormat, kDefaultSampleRate,
                            &audio_params_.channel_layout, audio_params_.sample_format,
                            audio_params_.sample_rate,
                            0, nullptr);
    
        if (!swr_ctx_ || swr_init(swr_ctx_) < 0) {
            log_e("Failed to initialize SwrContext");
            return false;
        }
    
        // Allocate resample buffer, +1 for rounding
        int max_out_samples = av_rescale_rnd(4096, kDefaultSampleRate, audio_params_.sample_rate, AV_ROUND_UP) + 1;
        resample_buffer_size_ = av_samples_get_buffer_size(nullptr, kDefaultChannels, max_out_samples,
                                                           kDefaultAVSampleFormat, 0);
        resample_buffer_ = (uint8_t *) av_malloc(resample_buffer_size_);
        return true;
    }
    void AudioPlayer::destroy_resampler() {
        if (swr_ctx_) {
            swr_free(&swr_ctx_);
            swr_ctx_ = nullptr;
        }
        if (resample_buffer_) {
            av_freep(&resample_buffer_);
            resample_buffer_ = nullptr;
        }
    }

    double AudioPlayer::get_clock() {
        // A more accurate clock would account for buffer latency
        return clock_;
    }

}