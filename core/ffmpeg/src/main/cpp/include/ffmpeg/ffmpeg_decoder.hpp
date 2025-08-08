#pragma once

#include "traits.hpp"
#include "ffmpeg_util.hpp"
#include <stdexcept>
#include <generator.hpp>

namespace bilias::ffmpeg {

    template<size_t N>
    class RingFrameBuffer {
        std::array<AVFrame *, N> buffer{};
        std::array<bool, N> is_ready{};
        size_t write_idx{0};
        size_t read_idx ={0};
        size_t ready_count{0};
        std::mutex mutex{};
        std::condition_variable cv_empty{};
        std::condition_variable cv_ready{};
        bool stopped{false};
    public:
        RingFrameBuffer() = default;

        ~RingFrameBuffer() {
            release(N);
        }

        auto init() -> void {
            for (int i = 0; i < N; ++i) {
                auto *frame = av_frame_alloc();
                if (!frame) [[unlikely]] {
                    release(i);
                    throw std::runtime_error("RingFrameBuffer::init");
                }
                buffer[i] = frame;
                is_ready[i] = false;
            }
            write_idx = 0;
            read_idx = 0;
            ready_count = 0;
        }

        // for decoder, get empty frame
        auto pop_empty_frame() -> AVFrame  * {
            std::unique_lock lock(mutex);
            cv_empty.wait(lock, [this]{
                return ready_count < N || stopped;
            });
            if (stopped) return nullptr;

            size_t idx = write_idx;
            while (is_ready[idx]) {
                idx = (idx + 1) % N;
            }

            write_idx = (idx + 1) % N;
            return buffer[idx];
        }

        // for decoder
        auto mark_frame_ready(AVFrame *frame) -> void {
            std::unique_lock lock(mutex);

            for (size_t i = 0; i < N; ++i) {
                if (buffer[i] == frame) {
                    is_ready[i] = true;
                    ready_count++;
                    cv_ready.notify_one();
                    return;
                }
            }
        }

        // for consumer
        auto pop_ready_frame() -> AVFrame * {
            std::unique_lock lock(mutex);
            cv_ready.wait(lock, [this]{
                return ready_count > 0 || stopped;
            });

            if (stopped && ready_count == 0) return nullptr;

            while (!is_ready[read_idx]) {
                read_idx = (read_idx + 1) % N;
            }

            auto *frame = buffer[read_idx];
            is_ready[read_idx] = false;
            ready_count--;
            read_idx = (read_idx + 1) % N;

            cv_empty.notify_one();
            return frame;
        }

        // for consumer
        auto return_frame(AVFrame *frame) -> void {
            av_frame_unref(frame);
        }




        auto stop() -> void {
            std::lock_guard lock(mutex);
            stopped = true;
            cv_empty.notify_all();
            cv_ready.notify_all();
        }


    private:
        auto release(size_t n) {
            for (int i = 0; i < n; ++i) {
                auto *ptr = std::exchange(buffer[i], nullptr);
                if (ptr) {
                    av_frame_free(&ptr);
                }
            }
        }
    };

    auto decode_frames(
        AVFormatContext *format_ctx,
        AVCodecContext *codec_ctx,
        int video_stream_index
    ) -> Generator<AVFrame *> ;

    class FFmpegDecoder final : NonCopy {
        int fd;
        AVIOContextPtr avio_ctx{nullptr};
        AVMallocPtr buffer{nullptr};
        AVFormatContextPtr format_ctx{nullptr};
        AVCodecContextPtr codec_ctx{nullptr};
        int video_stream_index{-1};


    public:
        explicit FFmpegDecoder(int fd) : fd(fd) {}

        auto init() -> void;

        auto new_generator() -> Generator<AVFrame *> {
            return decode_frames(format_ctx.get(), codec_ctx.get(), video_stream_index);
        }
    };
}
