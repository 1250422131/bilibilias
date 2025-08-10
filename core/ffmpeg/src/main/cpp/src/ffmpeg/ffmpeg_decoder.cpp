
#include <ffmpeg/ffmpeg_decoder.hpp>
#include <jni_log.hpp>
#include <stdexcept>
#include <thread>
#include <format>

extern "C" {
#include <libavcodec/avcodec.h>
}

namespace bilias::ffmpeg {

    auto decode_frames(
        AVFormatContext *format_ctx,
        AVCodecContext *codec_ctx,
        int video_stream_index
    ) -> Generator<AVFrame *> {


        try {
            constexpr auto buffer_size = 2 << 8;
            // co_await set_generator_buffer_size(buffer_size);
            auto buffer = RingFrameBuffer<buffer_size>();
            buffer.init();

            std::atomic<bool> running = true;

            std::thread decoder_thread([&] {
              AVPacket packet{};
              AVFrame *frame = buffer.pop_empty_frame();
              while (running && av_read_frame(format_ctx, &packet) >= 0) {
                  if (packet.stream_index != video_stream_index) {
                      av_packet_unref(&packet);
                      continue;
                  }

            //                if (!running || !frame) {
            //                    av_packet_unref(&packet);
            //                    break;
            //                }

                  int ret = avcodec_send_packet(codec_ctx, &packet);
                  av_packet_unref(&packet);

                  if (ret < 0) {
                      throw std::runtime_error(std::format("error avcodec_send_packet: {}", ret));
                  }

                  while (running) {
                      if (!frame) {
                          frame = buffer.pop_empty_frame();
                      }
                      ret = avcodec_receive_frame(codec_ctx, frame);
                      if (ret == AVERROR(EAGAIN) || ret == AVERROR_EOF) {
                          break;
                      } else if (ret < 0) {
                          throw std::runtime_error(std::format("error avcodec_receive_frame: {}", ret));
                      }
                      if (frame->format != AV_PIX_FMT_YUV420P) {
                          throw std::runtime_error(std::format("unsupported frame format: {}", frame->format));
                      }
                      buffer.mark_frame_ready_one();
                      frame = nullptr;
                  }
              }

              running = false;
              buffer.stop();
            });

            AVFrame *prev_frame{nullptr};

            while (true) {
                auto *frame = buffer.pop_ready_frame();
                if (!frame) {
                  log_i("decoder generator receive null");
                  break;
                }

                if (prev_frame) {
                  buffer.release_one();
                }
                co_yield static_cast<AVFrame *&&>(frame);
                prev_frame = frame;
            }

            if (prev_frame) {
              buffer.release_one();
            }

            decoder_thread.join();
            co_return;
        } catch (const std::exception &e) {
            log_e("error in decode_frames: {}", e.what());
        } catch (...) {
            log_e("unknown error in decode_frames");
        }
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
        if (!format_ctx) {
            throw std::runtime_error("Failed to create format context");
        }
        bilias_avformat_find_stream_info(format_ctx.get());

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

        codec_ctx = bilias_avcodec_alloc_context3(codec);
        if (!codec_ctx) {
            throw std::runtime_error("bilias_avcodec_alloc_context3 failed");
        }

        if (avcodec_parameters_to_context(codec_ctx.get(), codecpar) < 0) {
            throw std::runtime_error("avcodec_parameters_to_context failed");
        }

        codec_ctx->pix_fmt = AV_PIX_FMT_YUV420P;

        if (codec->id == AV_CODEC_ID_AV1) {
            codec_ctx->skip_frame = AVDISCARD_DEFAULT;
            codec_ctx->skip_idct = AVDISCARD_DEFAULT;
            codec_ctx->skip_loop_filter = AVDISCARD_DEFAULT;
        }

        if (avcodec_open2(codec_ctx.get(), codec, nullptr) < 0) {
            throw std::runtime_error("avcodec_open2 failed");
        }

    }
}
