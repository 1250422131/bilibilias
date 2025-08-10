
#include <ffmpeg/ffmpeg_util.hpp>
#include <unistd.h>
#include <unistd.h>
#include <fcntl.h>
#include <jni_log.hpp>

#include <stdexcept>

namespace {
    auto read_packet(void *opaque, uint8_t *buf, int buf_size) -> int {
        int fd = (int)(intptr_t)opaque;

        int ret;
        do {
            ret = read(fd, buf, buf_size);
        } while (ret < 0 && errno == EINTR);

        if (ret < 0) {
            return AVERROR(errno);
        }

        return ret;
    }

    auto seek_packet(void *opaque, int64_t offset, int whence) -> int64_t {
        int fd = (int)(intptr_t)opaque;
        off64_t ret = lseek64(fd, offset, whence);
        if (ret < 0) {
            return AVERROR(errno);
        }
        return ret;
    }
}

namespace bilias::ffmpeg {

    auto bilias_fd_bilias_avio_alloc_context(int fd) -> std::pair<AVIOContextPtr, AVMallocPtr> {
        auto buffer = bilias_av_malloc(1 << 14); // 16k
        auto ctx = bilias_avio_alloc_context(
            buffer.get(),
            4096,
            0,
            reinterpret_cast<void *>(fd),
            read_packet,
            nullptr,
            seek_packet
        );
        return {std::move(ctx), std::move(buffer)};
    }

    auto fd_create_and_bind_avframe(AVIOContext *avio_ctx) -> AVFormatContextPtr {
        auto frame = bilias_avformat_alloc_context();
        frame->pb = avio_ctx;
        auto frame_ptr = frame.get();
        if (auto ret = avformat_open_input(&frame_ptr, nullptr, nullptr, nullptr); ret  < 0) {
            char errbuf[256]{};
            av_strerror(ret, errbuf, sizeof(errbuf));
            throw std::runtime_error("avformat_open_input failed");
        }
        return frame;
    }

    auto bilias_avformat_find_stream_info(AVFormatContext *format_ctx) -> void {
        if (format_ctx == nullptr) {
            throw std::runtime_error("format_ctx is nullptr");
        }
        if (avformat_find_stream_info(format_ctx, nullptr) < 0) {
            throw std::runtime_error("can not find stream info");
        }
    }

    auto debug_av1_support() -> void {
        log_i("=== AV1 Debug Info ===");
        log_i("FFmpeg version: {}", av_version_info());
        const AVCodec *codec{};
        void *iter{};
        int av1_codec_count{};

        while ((codec = av_codec_iterate(&iter))) {
            if (codec->id == AV_CODEC_ID_AV1) {
                av1_codec_count++;
                std::string_view  type = av_codec_is_encoder(codec) ? "Encoder" : "Decoder";
                log_i("Found AV! {}: {} ({})", type, codec->name, codec->long_name ? codec->long_name : "No Description");
                log_i("Capabilities: {}", codec->capabilities);
            }
        }

        if (av1_codec_count == 0) {
            log_e("No AV1 codecs found at all!");
        } else {
            log_i("Found {} AV1 codec(s)", av1_codec_count);
        }
    }
}
