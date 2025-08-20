#pragma once

#include <traits.hpp>
#include <ffmpeg/ffmpeg_free.hpp>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <memory>
#include <atomic>
#include <queue>
#include <mutex>
#include <condition_variable>
#include <aaudio/AAudio.h>


extern "C" {
#include <libavutil/frame.h>
#include <libswresample/swresample.h>
}


namespace bilias::audio {

    class AudioPlayer : NonCopy {
    private:
        SLObjectItf engine_obj{nullptr};
        SLEngineItf engine{nullptr};
        SLObjectItf output_mix{nullptr};
        SLObjectItf player_obj{nullptr};
        SLPlayItf player{nullptr};
        SLAndroidSimpleBufferQueueItf player_queue{nullptr};

        int sample_rate{};
        int channel_count{};
        int bits_per_sample{};

        ffmpeg::SwrContextPtr swr_ctx{nullptr};

    public:
        AudioPlayer() = default;

        auto init() -> void;
        auto create_engine() -> void;
        auto create_output_mix() -> void;
        auto create_audio_player() -> void;
    };

    class AAudioPlayer final {
    private:
        AAudioStream *stream{nullptr};

        int sample_rate{};
        int channel_count{};
        aaudio_format_t format{AAUDIO_FORMAT_PCM_I16};

        // Audio conversion
        SwrContext* swr_ctx = nullptr;
        uint8_t* converted_buffer = nullptr;
        int converted_buffer_size = 0;
        int max_frames_per_burst = 0;


        std::atomic<bool> initialized{false};
    public:
        AAudioPlayer();
        ~AAudioPlayer();

        auto init(AVFormatContext *ctx) -> void;
        auto play_frame(AVFrame *frame) -> void;
        auto start() -> bool;
        auto stop() -> void;
    };

}