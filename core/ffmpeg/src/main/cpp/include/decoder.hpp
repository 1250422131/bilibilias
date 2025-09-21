#pragma once

#include "ffmpeg/ffmpeg_decoder.hpp"
#include "threadsafe_queue.hpp"
#include <thread>
#include <atomic>
#include <string>

// Forward declaration
struct AVFrame;
struct AVPacket;

namespace bilias {

    class Decoder {
    public:
        explicit Decoder(int fd);
        ~Decoder();

        Decoder(const Decoder &) = delete;
        Decoder &operator=(const Decoder &) = delete;

        // Starts the decoding thread
        void start();

        // Stops the decoding thread
        void stop();

        // The queues for other components to pull frames from
        ThreadSafeQueue<AVFrame *> video_frame_queue;
        ThreadSafeQueue<AVFrame *> audio_frame_queue;

        // Proxy methods to get stream info from FFmpegDecoder
        AVRational get_video_time_base() const;
        AudioParams get_audio_params() const;

    private:
        void run_decode_loop();

        std::unique_ptr<ffmpeg::FFmpegDecoder> ffmpeg_decoder_;
        std::thread decode_thread_;
        std::atomic<bool> stop_flag_{false};
    };

}
