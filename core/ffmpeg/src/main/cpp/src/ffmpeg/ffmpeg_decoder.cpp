
#include <ffmpeg/ffmpeg_decoder.hpp>
#include <stdexcept>
#include <thread>

namespace bilias::ffmpeg {

    auto decode_frames(
            AVFormatContext *format_ctx,
            AVCodecContext *codec_ctx,
            int video_stream_index
    ) -> BufferedGenerator<AVFrame *> {

        constexpr auto buffer_size = 2 << 8;
        auto buffer = RingFrameBuffer<buffer_size>();
        buffer.init();

        std::atomic<bool> running = true;

        std::thread decoder_thread([&] {
            AVPacket packet{};
            while (running && av_read_frame(format_ctx, &packet) >= 0) {
                if (packet.stream_index != video_stream_index) {
                    av_packet_unref(&packet);
                    continue;
                }

                AVFrame *frame{};
                do {
                    auto o = buffer.try_pop();
                    if (o.has_value()) {
                        frame = o.value();
                    } else {
                        std::this_thread::yield();
                    }
                } while (!frame);

                avcodec_send_packet(codec_ctx, &packet);
                int ret = avcodec_receive_frame(codec_ctx, frame);
                av_packet_unref(&packet);

                if (ret == 0) {
                    while (!buffer.try_push(frame)) {
                        std::this_thread::yield();
                    }
                } else {
                    buffer.try_push(frame);
                }
            }
        });

        while (running) {
            auto frame = buffer.wait_pop();
            if (!frame) break;
            co_yield static_cast<AVFrame *&&>(frame);
        }

        running = false;
        decoder_thread.join();
        co_return;
    }


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
