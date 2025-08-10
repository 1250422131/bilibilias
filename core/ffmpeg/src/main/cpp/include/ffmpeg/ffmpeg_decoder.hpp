#pragma once

#include "traits.hpp"
#include "ffmpeg_util.hpp"
#include <stdexcept>
#include <generator.hpp>

namespace bilias::ffmpeg {

    template<size_t N>
    requires ((N % 2) == 0)
    class RingFrameBuffer final : NonCopy {
        std::array<AVFrame *, N> buffer{};
        size_t free_tail{0};
        size_t prep_tail{0};
        size_t ready_head{0};
        size_t cons_head{0};

        std::atomic<size_t> free_count{N};
        std::atomic<size_t> ready_count{0};

        std::mutex mutex{};
        std::condition_variable free_cv{};
        std::condition_variable ready_cv{};
        std::atomic<bool> stop_flag{false};

    public:
        RingFrameBuffer() = default;

        auto init() -> void {
            for (size_t i = 0; i < N; ++i) {
                auto *f = av_frame_alloc();
                if (!f) {
                    throw std::runtime_error("av_frame_alloc failed");
                }
                buffer[i] = f;
            }
        }

        ~RingFrameBuffer() {
            free_queue(N);
        }

        // 获取空闲帧 (解码线程调用)
        AVFrame *pop_empty_frame() {
            std::unique_lock lock(mutex);
            free_cv.wait(lock, [this] {
                return free_count.load() > 0 || stop_flag.load();;
            });
            if (stop_flag.load()) {
                return nullptr;
            }
            auto *f = buffer[free_tail];
            free_tail = (free_tail + 1) % N;
            free_count--;
            return f;
        }


        void mark_frame_ready_one() {
            std::lock_guard lock(mutex);
            ready_count++;
            ready_cv.notify_one();
        }

        AVFrame *pop_ready_frame() {
            std::unique_lock lock(mutex);
            ready_cv.wait(lock, [this] {
                return ready_count.load() > 0 || stop_flag.load();
            });
            if (stop_flag.load()) {
                if (ready_count.load() == 0) {
                    return nullptr;
                }
            }
            auto *f = buffer[ready_head];
            ready_head = (ready_head + 1) % N;
            ready_count--;
            return f;
        }

        void release_one() {
            std::lock_guard lock(mutex);
            free_count++;
            cons_head = (cons_head + 1) % N;
            free_cv.notify_one();
        }

        void stop() {
            stop_flag.store(true);
            free_cv.notify_all();
            ready_cv.notify_all();
        }

    private:

        auto free_queue(size_t size) -> void {
            for (size_t i = 0; i < size; ++i) {
                auto ptr = buffer[i];
                if (ptr) {
                    av_frame_free(&ptr);
                }
               buffer[i] = nullptr;
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
