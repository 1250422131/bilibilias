
#include <ffmpeg/ffmpeg_util.hpp>
#include <unistd.h>
#include <unistd.h>
#include <fcntl.h>

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
        auto buffer = bilias_av_malloc(4096);
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
}
