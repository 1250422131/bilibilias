
#include <ffmpeg/ffmpeg_decoder.hpp>
#include <stdexcept>

namespace bilias::ffmpeg {


    auto FFmpegDecoder::init() -> void {
        {
            auto [ctx, buf] = bilias_fd_bilias_avio_alloc_context(fd);
            if (ctx && buf) [[likely]] {
                this->avio_ctx = std::move(ctx);
                this->buffer = std::move(buf);
            } else {
                throw std::runtime_error("bilias_fd_bilias_avio_alloc_context failed");
            }
        }

        this->format_ctx = fd_create_and_bind_avframe(this->avio_ctx.get());
        bilias_avformat_find_stream_info(format_ctx.get());

        int video_stream_index = -1;
        for (int i = 0; i < format_ctx->nb_streams; i++) {
            if (format_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
                video_stream_index = i;
                break;
            }
        }
        if (video_stream_index == -1) {
            throw std::runtime_error("no video stream found");
        }

        auto *steam = format_ctx->streams[video_stream_index];
        auto *codecpar = steam->codecpar;
        auto *codec = avcodec_find_decoder(codecpar->codec_id);
        if (!codec) {
            throw std::runtime_error("avcodec_find_decoder failed");
        }

        auto codec_ctx = bilias_avcodec_alloc_context3(codec);
        if (!codec_ctx) {
            throw std::runtime_error("bilias_avcodec_alloc_context3 failed");
        }

        if (avcodec_parameters_to_context(codec_ctx.get(), codecpar) < 0) {
            throw std::runtime_error("avcodec_parameters_to_context failed");
        }

        if (avcodec_open2(codec_ctx.get(), codec, nullptr) < 0) {
            throw std::runtime_error("avcodec_open2 failed");
        }

    }
}