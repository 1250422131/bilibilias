#pragma once

#include "traits.hpp"

extern "C" {
#include "libavformat/avformat.h"
}

namespace bilias::ffmpeg {

    class FFMPEGFree {
    public:

        auto operator()(AVFormatContext *ctx) const noexcept -> void {
            if (ctx) {
                avformat_free_context(ctx);
            }
        }
    };

    using AVFormatContextPtr = std::unique_ptr<AVFormatContext, FFMPEGFree>;

    inline auto bilias_avformat_alloc_output_context2(
        const AVOutputFormat *oformat,
        const char *format_name,
        const char *filename
    ) noexcept -> std::pair<AVFormatContextPtr, int> {
        AVFormatContext *temp{nullptr};
        auto ret = avformat_alloc_output_context2(&temp, oformat, format_name, filename);
        return {AVFormatContextPtr(temp), ret};
    }

} // namespace bilias::ffmpeg
