#pragma once

#include "threadsafe_queue.hpp"
#include <oboe/Oboe.h>
#include <atomic>

extern "C" {
#include "libavcodec/avcodec.h"
#include "libswresample/swresample.h"
#include "libavutil/rational.h"
}

// Forward declaration
struct AVFrame;

namespace bilias {

    struct AudioParams {
        int sample_rate;
        int channels;
        AVSampleFormat sample_format;
        AVChannelLayout channel_layout;
        AVRational time_base;
    };

    class AudioPlayer : public oboe::AudioStreamDataCallback {
    public:
        explicit AudioPlayer(ThreadSafeQueue<AVFrame *> &frame_queue);
        ~AudioPlayer();

        AudioPlayer(const AudioPlayer &) = delete;
        AudioPlayer &operator=(const AudioPlayer &) = delete;

        // Initializes and starts the Oboe audio stream
        bool start(AudioParams params);

        // Stops and closes the Oboe audio stream
        void stop();

        // Implementation of Oboe's callback
        oboe::DataCallbackResult onAudioReady(
                oboe::AudioStream *oboeStream,
                void *audioData,
                int32_t numFrames) override;

        // Gets the current audio clock time in seconds
        double get_clock();

    private:
        bool init_resampler();
        void destroy_resampler();

        ThreadSafeQueue<AVFrame *> &audio_frame_queue_;
        std::shared_ptr<oboe::AudioStream> stream_;
        std::atomic<bool> is_playing_{false};

        // Resampling
        SwrContext *swr_ctx_{nullptr};
        uint8_t *resample_buffer_{nullptr};
        size_t resample_buffer_size_{0};
        AudioParams audio_params_{};

        // Clock
        double clock_{0.0};
        AVFrame *current_frame_{nullptr};
        int current_frame_offset_{0};
        
        // Internal buffer state for drain-fill model
        uint8_t *resample_buffer_ptr_{nullptr};
        int resampled_data_size_{0};
    };

}
