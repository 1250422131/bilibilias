#pragma once

#include <ffmpeg/ffmpeg_free.hpp>

namespace bilias::ffmpeg {

    auto bilias_fd_bilias_avio_alloc_context(int fd) -> std::pair<AVIOContextPtr, AVMallocPtr>;

    auto fd_create_and_bind_avframe(AVIOContext *avio_ctx) -> AVFormatContextPtr;

    auto bilias_avformat_find_stream_info(AVFormatContext *format_ctx) -> void;

}
