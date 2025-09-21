#include "ffmpeg/ffmpeg_decoder.hpp"
#include "jni_log.hpp"
#include <stdexcept>

extern "C" {
#include "libavutil/frame.h"
#include "libavutil/channel_layout.h"
}

namespace bilias::ffmpeg {

    // Helper to open a codec context for a given stream
    static AVCodecContextPtr open_codec_context(AVFormatContext *fmt_ctx, AVMediaType type, int &stream_idx) {
        int ret = av_find_best_stream(fmt_ctx, type, -1, -1, nullptr, 0);
        if (ret < 0) {
            log_w("Could not find stream of type %s", av_get_media_type_string(type));
            return nullptr;
        }
        stream_idx = ret;

        AVStream *st = fmt_ctx->streams[stream_idx];
        const AVCodec *dec = avcodec_find_decoder(st->codecpar->codec_id);
        if (!dec) {
            throw std::runtime_error("Failed to find codec");
        }

        AVCodecContextPtr cod_ctx = bilias_avcodec_alloc_context3(dec);
        if (avcodec_parameters_to_context(cod_ctx.get(), st->codecpar) < 0) {
            throw std::runtime_error("Failed to copy codec parameters to context");
        }

        if (avcodec_open2(cod_ctx.get(), dec, nullptr) < 0) {
            throw std::runtime_error("Failed to open codec");
        }
        return cod_ctx;
    }

    FFmpegDecoder::FFmpegDecoder(int fd) : fd_(fd) {}

    FFmpegDecoder::~FFmpegDecoder() = default;

    void FFmpegDecoder::init() {
        auto [ctx, buf] = bilias_fd_bilias_avio_alloc_context(fd_);
        avio_ctx_ = std::move(ctx);
        buffer_ = std::move(buf);

        format_ctx_ = fd_create_and_bind_avframe(avio_ctx_.get());
        bilias_avformat_find_stream_info(format_ctx_.get());

        video_codec_ctx_ = open_codec_context(format_ctx_.get(), AVMEDIA_TYPE_VIDEO, video_stream_index_);
        audio_codec_ctx_ = open_codec_context(format_ctx_.get(), AVMEDIA_TYPE_AUDIO, audio_stream_index_);

        if (!video_codec_ctx_ && !audio_codec_ctx_) {
            throw std::runtime_error("Failed to open any codecs");
        }
        log_i("FFmpegDecoder initialized successfully");
    }

    int FFmpegDecoder::read_frame(AVPacket *packet) {
        return av_read_frame(format_ctx_.get(), packet);
    }

    void FFmpegDecoder::decode_packet(AVPacket *packet, const std::function<void(AVFrame *)> &on_frame_decoded) {
        AVCodecContext *target_ctx = nullptr;
        if (packet->stream_index == video_stream_index_) {
            target_ctx = video_codec_ctx_.get();
        } else if (packet->stream_index == audio_stream_index_) {
            target_ctx = audio_codec_ctx_.get();
        } else {
            return; // Not a stream we are interested in
        }

        int ret = avcodec_send_packet(target_ctx, packet);
        if (ret < 0) {
            log_e("Error sending packet for decoding");
            return;
        }

        while (ret >= 0) {
            AVFrame *frame = av_frame_alloc();
            ret = avcodec_receive_frame(target_ctx, frame);
            if (ret == AVERROR(EAGAIN) || ret == AVERROR_EOF) {
                av_frame_free(&frame);
                break;
            } else if (ret < 0) {
                log_e("Error during decoding");
                av_frame_free(&frame);
                break;
            }
            on_frame_decoded(frame); // Ownership is transferred to the callback
        }
    }

    AVRational FFmpegDecoder::get_video_time_base() const {
        if (video_stream_index_ != -1) {
            return format_ctx_->streams[video_stream_index_]->time_base;
        }
        return {0, 1}; // Return a default rational
    }

    AudioParams FFmpegDecoder::get_audio_params() const {
        if (audio_codec_ctx_) {
            return {
                    .sample_rate = audio_codec_ctx_->sample_rate,
                    .channels = audio_codec_ctx_->ch_layout.nb_channels,
                    .sample_format = audio_codec_ctx_->sample_fmt,
                    .channel_layout = audio_codec_ctx_->ch_layout,
                    .time_base = format_ctx_->streams[audio_stream_index_]->time_base
            };
        }
        return {};
    }
}
