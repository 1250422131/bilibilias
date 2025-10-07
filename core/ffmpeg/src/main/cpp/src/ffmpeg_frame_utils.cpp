#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <vector>
#include <sstream>
#include <iomanip>
#include "ffmpeg_free.hpp"

extern "C" {
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libavutil/avutil.h"
#include "libavutil/opt.h"
#include "libavutil/timestamp.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
}

using namespace bilias::ffmpeg;

namespace {
    class FFmpegFrameListener {
        JNIEnv *env;
        jobject o_listener;
        jmethodID m_on_frame;
        jmethodID m_on_progress;
        jmethodID m_on_complete;
    public:
        FFmpegFrameListener(JNIEnv *env, jobject o_listener, jmethodID m_on_frame, jmethodID m_on_progress, jmethodID m_on_complete)
            : env(env), o_listener(o_listener), m_on_frame(m_on_frame), m_on_progress(m_on_progress), m_on_complete(m_on_complete) {}
        void on_frame(const uint8_t *data, int width, int height, int index) {
            jbyteArray arr = env->NewByteArray(width * height * 3); // RGB24
            env->SetByteArrayRegion(arr, 0, width * height * 3, reinterpret_cast<const jbyte *>(data));
            env->CallVoidMethod(o_listener, m_on_frame, arr, width, height, index);
            env->DeleteLocalRef(arr);
        }
        void on_progress(int progress) {
            env->CallVoidMethod(o_listener, m_on_progress, progress);
        }
        void on_complete() {
            env->CallVoidMethod(o_listener, m_on_complete);
        }
        static FFmpegFrameListener create(JNIEnv *env, jobject listener) {
            auto listenerCls = env->GetObjectClass(listener);
            auto onFrame = env->GetMethodID(listenerCls, "onFrame", "([BIII)V");
            auto onProgress = env->GetMethodID(listenerCls, "onProgress", "(I)V");
            auto onComplete = env->GetMethodID(listenerCls, "onComplete", "()V");
            return FFmpegFrameListener(env, listener, onFrame, onProgress, onComplete);
        }
    };
}

// 获取视频帧率 JNI 方法
extern "C" JNIEXPORT jint JNICALL
Java_com_imcys_bilibilias_ffmpeg_FFmpegManger_getVideoFrameRate(
        JNIEnv *env,
        jobject thiz,
        jstring videoPath_) {
    const char *videoPath = env->GetStringUTFChars(videoPath_, 0);
    AVFormatContext *fmt_ctx = nullptr;
    int ret = avformat_open_input(&fmt_ctx, videoPath, nullptr, nullptr);
    if (ret < 0) {
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        return -1;
    }
    ret = avformat_find_stream_info(fmt_ctx, nullptr);
    if (ret < 0) {
        avformat_close_input(&fmt_ctx);
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        return -1;
    }
    int frame_rate = -1;
    for (unsigned int i = 0; i < fmt_ctx->nb_streams; i++) {
        if (fmt_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            AVRational r = fmt_ctx->streams[i]->avg_frame_rate;
            if (r.num > 0 && r.den > 0) {
                frame_rate = (int)(r.num / r.den);
            }
            break;
        }
    }
    avformat_close_input(&fmt_ctx);
    env->ReleaseStringUTFChars(videoPath_, videoPath);
    return frame_rate;
}

// 获取视频帧 JNI 方法
extern "C" JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_ffmpeg_FFmpegManger_getVideoFramesJNI(
        JNIEnv *env,
        jobject thiz,
        jstring videoPath_,
        jint framesPerSecond,
        jobject listener) {
    const char *videoPath = env->GetStringUTFChars(videoPath_, 0);
    auto callback = FFmpegFrameListener::create(env, listener);
    AVFormatContext *fmt_ctx = nullptr;
    int ret = avformat_open_input(&fmt_ctx, videoPath, nullptr, nullptr);
    if (ret < 0) {
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        callback.on_complete();
        return;
    }
    ret = avformat_find_stream_info(fmt_ctx, nullptr);
    if (ret < 0) {
        avformat_close_input(&fmt_ctx);
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        callback.on_complete();
        return;
    }
    int video_stream_index = -1;
    for (unsigned int i = 0; i < fmt_ctx->nb_streams; i++) {
        if (fmt_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_stream_index = i;
            break;
        }
    }
    if (video_stream_index == -1) {
        avformat_close_input(&fmt_ctx);
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        callback.on_complete();
        return;
    }
    AVCodecParameters *codecpar = fmt_ctx->streams[video_stream_index]->codecpar;
    const AVCodec *codec = avcodec_find_decoder(codecpar->codec_id);
    if (!codec) {
        avformat_close_input(&fmt_ctx);
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        callback.on_complete();
        return;
    }
    AVCodecContext *codec_ctx = avcodec_alloc_context3(codec);
    avcodec_parameters_to_context(codec_ctx, codecpar);
    if (avcodec_open2(codec_ctx, codec, nullptr) < 0) {
        avcodec_free_context(&codec_ctx);
        avformat_close_input(&fmt_ctx);
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        callback.on_complete();
        return;
    }
    // 获取视频时长（秒）
    double duration_sec = 0.0;
    if (fmt_ctx->streams[video_stream_index]->duration > 0) {
        duration_sec = fmt_ctx->streams[video_stream_index]->duration * av_q2d(fmt_ctx->streams[video_stream_index]->time_base);
    } else if (fmt_ctx->duration > 0) {
        duration_sec = fmt_ctx->duration / (double)AV_TIME_BASE;
    }
    if (duration_sec <= 0.0) duration_sec = 1.0; // 防止异常
    int total_target_frames = (int)(framesPerSecond * duration_sec + 0.5); // 四舍五入
    if (total_target_frames < 1) total_target_frames = 1;
    // 计算每一帧的目标采样时间点（秒）
    std::vector<double> target_times;
    for (int i = 0; i < total_target_frames; ++i) {
        target_times.push_back(i / (double)framesPerSecond);
    }
    size_t next_target = 0;
    AVPacket *pkt = av_packet_alloc();
    AVFrame *frame = av_frame_alloc();
    AVFrame *rgb_frame = av_frame_alloc();
    int width = codec_ctx->width;
    int height = codec_ctx->height;
    int num_bytes = av_image_get_buffer_size(AV_PIX_FMT_RGB24, width, height, 1);
    uint8_t *buffer = (uint8_t *)av_malloc(num_bytes);
    av_image_fill_arrays(rgb_frame->data, rgb_frame->linesize, buffer, AV_PIX_FMT_RGB24, width, height, 1);
    SwsContext *sws_ctx = sws_getContext(width, height, codec_ctx->pix_fmt,
                                         width, height, AV_PIX_FMT_RGB24,
                                         SWS_BILINEAR, nullptr, nullptr, nullptr);
    int saved_index = 0;
    int last_progress = 0;
    bool finished = false;
    while (av_read_frame(fmt_ctx, pkt) >= 0 && next_target < target_times.size()) {
        if (pkt->stream_index == video_stream_index) {
            if (avcodec_send_packet(codec_ctx, pkt) == 0) {
                while (avcodec_receive_frame(codec_ctx, frame) == 0 && next_target < target_times.size()) {
                    // 当前帧的pts转为秒
                    double pts_sec = 0.0;
                    if (frame->pts != AV_NOPTS_VALUE) {
                        pts_sec = frame->pts * av_q2d(fmt_ctx->streams[video_stream_index]->time_base);
                    } else if (frame->best_effort_timestamp != AV_NOPTS_VALUE) {
                        pts_sec = frame->best_effort_timestamp * av_q2d(fmt_ctx->streams[video_stream_index]->time_base);
                    }
                    // 采样：只要pts_sec >= 目标采样点
                    while (next_target < target_times.size() && pts_sec >= target_times[next_target]) {
                        sws_scale(sws_ctx, frame->data, frame->linesize, 0, height, rgb_frame->data, rgb_frame->linesize);
                        callback.on_frame(buffer, width, height, saved_index);
                        saved_index++;
                        int progress = (int)(100.0 * saved_index / total_target_frames);
                        if (progress > last_progress && progress <= 100) {
                            callback.on_progress(progress);
                            last_progress = progress;
                        }
                        next_target++;
                    }
                    if (next_target >= target_times.size()) {
                        finished = true;
                        break;
                    }
                }
            }
        }
        av_packet_unref(pkt);
        if (finished) break;
    }
    sws_freeContext(sws_ctx);
    av_free(buffer);
    av_frame_free(&rgb_frame);
    av_frame_free(&frame);
    av_packet_free(&pkt);
    avcodec_free_context(&codec_ctx);
    avformat_close_input(&fmt_ctx);
    env->ReleaseStringUTFChars(videoPath_, videoPath);
    callback.on_complete();
}
