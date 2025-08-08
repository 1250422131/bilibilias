#pragma once

#include "traits.hpp"
#include "ffmpeg_util.hpp"
#include <stdexcept>
#include <generator.hpp>

namespace bilias::ffmpeg {

    template<size_t N>
    class RingFrameBuffer {
        std::array<AVFrame *, N> buffer{};
        size_t write_idx{0};
        size_t read_idx ={0};
        std::mutex mutex{};
        std::condition_variable cv{};
    public:
        RingFrameBuffer() = default;

        ~RingFrameBuffer() {
            release(N - 1);
        }

        auto init() -> void {
            for (int i = 0; i < N; ++i) {
                auto *frame = av_frame_alloc();
                if (!frame) [[unlikely]] {
                    release(i);
                    throw std::runtime_error("RingFrameBuffer::init");
                }
                buffer[i] = frame;
            }
        }

        auto try_push(AVFrame *frame) -> bool {
            std::unique_lock lock(mutex, std::try_to_lock);
            if (!lock || (write_idx + 1) % N == read_idx) {
                return false;
            }

            buffer[write_idx] = frame;
            write_idx = (write_idx + 1) % N;
            cv.notify_one();
            return true;
        }

        auto try_pop() -> std::optional<AVFrame *>{
            std::unique_lock lock(mutex, std::try_to_lock);
            if (!lock || write_idx == read_idx) {
                return std::nullopt;
            }
            auto *frame = buffer[read_idx];
            read_idx = (read_idx + 1) % N;
            return frame;
        }

        auto wait_pop() -> AVFrame * {
            std::unique_lock lock(mutex);
            cv.wait(lock, [this]{ return write_idx != read_idx; });

            auto *frame = buffer[read_idx];
            read_idx = (read_idx + 1) % N;
            return frame;
        }

        auto wait_push(AVFrame *frame) -> void {
            std::unique_lock lock(mutex);
            cv.wait(lock, [this] {
                return (write_idx + 1) % N != read_idx;
            });

            buffer[write_idx] = frame;
            write_idx = (write_idx + 1) % N;
            cv.notify_one();
        }

        auto notify_all() noexcept {
            cv.notify_all();
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
    ) -> BufferedGenerator<AVFrame *>;

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

        auto new_generator() -> BufferedGenerator<AVFrame *> {
            return decode_frames(format_ctx.get(), codec_ctx.get(), video_stream_index);
        }
    };
}
