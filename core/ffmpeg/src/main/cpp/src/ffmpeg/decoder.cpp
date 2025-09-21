#include "decoder.hpp"
#include <ffmpeg/ffmpeg_free.hpp>
#include "jni_log.hpp"

extern "C" {
#include "libavutil/frame.h"
}

namespace bilias {

    Decoder::Decoder(int fd) {
        ffmpeg_decoder_ = std::make_unique<ffmpeg::FFmpegDecoder>(fd);
        ffmpeg_decoder_->init();
    }

    Decoder::~Decoder() {
        stop();
        // Clear queues
        AVFrame *frame;
        while (video_frame_queue.try_pop(frame)) {
            av_frame_free(&frame);
        }
        while (audio_frame_queue.try_pop(frame)) {
            av_frame_free(&frame);
        }
    }

    void Decoder::start() {
        stop_flag_.store(false);
        decode_thread_ = std::thread(&Decoder::run_decode_loop, this);
    }

    void Decoder::stop() {
        stop_flag_.store(true);
        if (decode_thread_.joinable()) {
            decode_thread_.join();
        }
    }

    void Decoder::run_decode_loop() {
        log_i("Decoder loop started");
        ffmpeg::AVPacketPtr packet(av_packet_alloc());

        while (!stop_flag_.load()) {
            // TODO: Add queue size check to avoid memory explosion

            auto read_result = ffmpeg_decoder_->read_frame(packet.get());
            if (read_result < 0) {
                // EOF or error
                log_i("Decoder loop finished: EOF or error");
                break;
            }

            if (packet->stream_index == ffmpeg_decoder_->get_video_stream_index()) {
                ffmpeg_decoder_->decode_packet(packet.get(), [&](AVFrame *frame) {
                    video_frame_queue.push(frame);
                });
            } else if (packet->stream_index == ffmpeg_decoder_->get_audio_stream_index()) {
                ffmpeg_decoder_->decode_packet(packet.get(), [&](AVFrame *frame) {
                    audio_frame_queue.push(frame);
                });
            }

            av_packet_unref(packet.get());
        }

        log_i("Decoder loop stopped");
    }

    // Expose FFmpegDecoder methods
    AVRational Decoder::get_video_time_base() const {
        return ffmpeg_decoder_->get_video_time_base();
    }

    AudioParams Decoder::get_audio_params() const {
        return ffmpeg_decoder_->get_audio_params();
    }

}
