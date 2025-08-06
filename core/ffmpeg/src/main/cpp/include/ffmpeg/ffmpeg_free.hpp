#pragma once

#include <memory>
#include "traits.hpp"

extern "C" {
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
}


namespace bilias::ffmpeg {

    class FFMPEGFree {
    public:

        auto operator()(AVIOContext *ctx) const noexcept -> void {
            if (ctx) {
                auto *ptr = ctx;
                avio_context_free(&ptr);
            }
        }

        auto operator()(AVFormatContext *ctx) const noexcept -> void {
            if (ctx) {
                avformat_free_context(ctx);
            }
        }

        auto operator()(SwsContext *ctx) const noexcept -> void {
            if (ctx) {
                sws_freeContext(ctx);
            }
        }

        auto operator()(AVFrame *frame) const noexcept -> void {
            if (frame) {
                auto *ptr = frame;
                av_frame_free(&ptr);
            }
        }

        auto operator()(AVPacket *packet) const noexcept -> void {
            if (packet) {
                auto *ptr = packet;
                av_packet_free(&ptr);
            }
        }

        auto operator()(AVCodecContext *ctx) const noexcept -> void {
            if (ctx) {
                auto *ptr = ctx;
                avcodec_free_context(&ptr);
            }
        }

        auto operator()(AVCodecContext **ctx) const noexcept -> void {
            if (ctx) {
                auto *ptr = *ctx;
                avcodec_free_context(&ptr);
            }
        }

        auto operator()(uint8_t *av_buf) const noexcept -> void {
            if (av_buf) {
                av_free(av_buf);
            }
        }
    };

    using AVIOContextPtr = std::unique_ptr<AVIOContext, FFMPEGFree>;
    using AVFormatContextPtr = std::unique_ptr<AVFormatContext, FFMPEGFree>;
    using SwsContextPtr = std::unique_ptr<SwsContext, FFMPEGFree>;
    using AVFramePtr = std::unique_ptr<AVFrame, FFMPEGFree>;
    using AVPacketPtr = std::unique_ptr<AVPacket, FFMPEGFree>;
    using AVCodecContextPtr = std::unique_ptr<AVCodecContext, FFMPEGFree>;
    using AVCodecPtr = std::unique_ptr<AVCodec, FFMPEGFree>;
    using AVMallocPtr = std::unique_ptr<uint8_t, FFMPEGFree>;

    inline auto bilias_av_malloc(size_t size) noexcept -> AVMallocPtr {
        return AVMallocPtr(reinterpret_cast<uint8_t *>(av_malloc(size)));
    }

    inline auto bilias_avcodec_alloc_context3(const AVCodec *codec) -> AVCodecContextPtr {
        return AVCodecContextPtr(avcodec_alloc_context3(codec));
    }

    inline auto bilias_avformat_alloc_context() -> AVFormatContextPtr {
        return AVFormatContextPtr(avformat_alloc_context());
    }

    inline auto bilias_avformat_alloc_output_context2(
        const AVOutputFormat *oformat,
        const char *format_name,
        const char *filename
    ) noexcept -> std::pair<AVFormatContextPtr, int> {
        AVFormatContext *temp{nullptr};
        auto ret = avformat_alloc_output_context2(&temp, oformat, format_name, filename);
        return {AVFormatContextPtr(temp), ret};
    }

    inline auto bilias_avio_alloc_context(
            unsigned char *buffer,
            int buffer_size,
            int write_flag,
            void *opaque,
            int (*read_packet)(void *opaque, uint8_t *buf, int buf_size),
            int (*write_packet)(void *opaque, const uint8_t *buf, int buf_size),
            int64_t (*seek)(void *opaque, int64_t offset, int whence)
    ) -> AVIOContextPtr {
        return AVIOContextPtr(avio_alloc_context(buffer, buffer_size, write_flag, opaque, read_packet, write_packet, seek));
    }


} // namespace bilias::ffmpeg
