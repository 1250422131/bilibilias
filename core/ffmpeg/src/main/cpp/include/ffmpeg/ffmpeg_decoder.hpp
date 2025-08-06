#pragma once

#include "traits.hpp"
#include "ffmpeg_util.hpp"
#include <stdexcept>

namespace bilias::ffmpeg {

    class FFmpegDecoder final : NonCopy {
        int fd;
        AVIOContextPtr avio_ctx{nullptr};
        AVMallocPtr buffer{nullptr};
        AVFormatContextPtr format_ctx{nullptr};

    public:
        explicit FFmpegDecoder(int fd) : fd(fd) {}

        auto init() -> void;
    };
}