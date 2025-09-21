#include <jni.h>
#include <string>
#include <map>
#include <mutex>
#include <string_view>

#include <traits.hpp>
#include <ffmpeg/ffmpeg_free.hpp>

extern "C" {
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libavutil/avutil.h"
#include "libavutil/opt.h"
#include "libavutil/timestamp.h"
#include "libswresample/swresample.h"
#include "libswscale/swscale.h"
}

using namespace bilias;
using namespace bilias::ffmpeg;

namespace {
    
    class FFmpegMergeListener {
        JNIEnv *env;
        jobject o_listener;
        jmethodID m_on_error;
        jmethodID m_on_complete;
        jmethodID m_on_progress;
        
    public:
        FFmpegMergeListener(
            JNIEnv *env, 
            jobject o_listener,
            jmethodID m_on_error,
            jmethodID m_on_complete,
            jmethodID m_on_progress
        ) noexcept : env(env), o_listener(o_listener), m_on_error(m_on_error),
        m_on_complete(m_on_complete), m_on_progress(m_on_progress) {}
        
        auto on_error(std::string_view msg) {
            env->CallVoidMethod(o_listener, m_on_error, env->NewStringUTF(msg.data()));
        }
        
        auto on_complete() {
            env->CallVoidMethod(o_listener, m_on_complete);
        }
        
        auto on_progress(int progress) {
            env->CallVoidMethod(o_listener, m_on_progress, progress);
        }
        
        static auto create(JNIEnv *env, jobject listener) -> FFmpegMergeListener {
            auto listenerCls = env->GetObjectClass(listener);
            auto onError = env->GetMethodID(listenerCls, "onError", "(Ljava/lang/String;)V");
            auto onComplete = env->GetMethodID(listenerCls, "onComplete", "()V");
            auto onProgress = env->GetMethodID(listenerCls, "onProgress", "(I)V");
            return FFmpegMergeListener{env, listener, onError, onComplete, onProgress};
        }
    };
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_imcys_bilibilias_ffmpeg_FFmpegManger_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_imcys_bilibilias_ffmpeg_FFmpegManger_getFFmpegVersion(
        JNIEnv *env,
        jobject /* this */) {
    return env->NewStringUTF(av_version_info());
}

// 合并音视频 JNI 方法
extern "C" JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_ffmpeg_FFmpegManger_mergeVideoAndAudio(
        JNIEnv *env,
        jobject thiz,
        jstring videoPath_,
        jstring audioPath_,
        jstring outputPath_,
        jstring title_,
        jstring description_,
        jstring copyright_,
        jobject listener) {
    const char *videoPath = env->GetStringUTFChars(videoPath_, 0);
    const char *audioPath = env->GetStringUTFChars(audioPath_, 0);
    const char *outputPath = env->GetStringUTFChars(outputPath_, 0);
    const char *title = env->GetStringUTFChars(title_, 0);
    const char *description = env->GetStringUTFChars(description_, 0);
    const char *copyright = env->GetStringUTFChars(copyright_, 0);

    auto _defer = Defer{[&] {
        env->ReleaseStringUTFChars(videoPath_, videoPath);
        env->ReleaseStringUTFChars(audioPath_, audioPath);
        env->ReleaseStringUTFChars(outputPath_, outputPath);
        env->ReleaseStringUTFChars(title_, title);
        env->ReleaseStringUTFChars(description_, description);
        env->ReleaseStringUTFChars(copyright_, copyright);
    }};
    
    auto callback = FFmpegMergeListener::create(env, listener);

    AVFormatContext *ifmt_ctx_v = nullptr, *ifmt_ctx_a = nullptr;
    int ret;
    int video_index_in = -1, audio_index_in = -1;
    int video_index_out = -1, audio_index_out = -1;
    int64_t video_pts = 0, audio_pts = 0;

    // 打开视频输入
    if ((ret = avformat_open_input(&ifmt_ctx_v, videoPath, nullptr, nullptr)) < 0) {
        callback.on_error("无法打开视频文件");
        return;
    }
    if ((ret = avformat_find_stream_info(ifmt_ctx_v, nullptr)) < 0) {
        callback.on_error("无法获取视频流信息");
        return;
    }

    // 打开音频输入
    if ((ret = avformat_open_input(&ifmt_ctx_a, audioPath, nullptr, nullptr)) < 0) {
        callback.on_error("无法打开音频文件");
        return;
    }
    if ((ret = avformat_find_stream_info(ifmt_ctx_a, nullptr)) < 0) {
        callback.on_error("无法获取音频流信息");
        return;
    }

    // 创建输出
    auto [ofmt_ctx, ctx_result] = bilias_avformat_alloc_output_context2(nullptr, nullptr, outputPath);

    if (!ofmt_ctx) {
        callback.on_error("无法创建输出文件");
        return;
    }

    // 设置元信息
    av_dict_set(&ofmt_ctx->metadata, "title", title, 0);
    av_dict_set(&ofmt_ctx->metadata, "description", description, 0);
    av_dict_set(&ofmt_ctx->metadata, "copyright", copyright, 0);

    // 拷贝视频流
    for (unsigned int i = 0; i < ifmt_ctx_v->nb_streams; i++) {
        if (ifmt_ctx_v->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            AVStream *in_stream = ifmt_ctx_v->streams[i];
            AVStream *out_stream = avformat_new_stream(ofmt_ctx.get(), nullptr);
            video_index_in = i;
            video_index_out = out_stream->index;
            avcodec_parameters_copy(out_stream->codecpar, in_stream->codecpar);
            // 默认编码
            // out_stream->codecpar->codec_tag = 0;
            break;
        }
    }
    // 拷贝音频流
    for (unsigned int i = 0; i < ifmt_ctx_a->nb_streams; i++) {
        if (ifmt_ctx_a->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            AVStream *in_stream = ifmt_ctx_a->streams[i];
            AVStream *out_stream = avformat_new_stream(ofmt_ctx.get(), nullptr);
            audio_index_in = i;
            audio_index_out = out_stream->index;
            avcodec_parameters_copy(out_stream->codecpar, in_stream->codecpar);
            out_stream->codecpar->codec_tag = 0;
            break;
        }
    }
    if (video_index_in == -1 || audio_index_in == -1) {
        jclass listenerCls = env->GetObjectClass(listener);
        jmethodID onError = env->GetMethodID(listenerCls, "onError", "(Ljava/lang/String;)V");
        env->CallVoidMethod(listener, onError, env->NewStringUTF("未找到有效的视频或音频流"));
        return;
    }

    // 打开输出文件
    if (!(ofmt_ctx->oformat->flags & AVFMT_NOFILE)) {
        if (avio_open(&ofmt_ctx->pb, outputPath, AVIO_FLAG_WRITE) < 0) {
            callback.on_error("无法打开输出文件");
            return;
        }
    }

    // 为了保留杜比视界等非标准元数据，需要设置 "strict" 选项
    // 这等同于命令行中的 -strict unofficial
    av_opt_set(ofmt_ctx.get(), "strict", "unofficial", 0);
    // ----------------------

    // 写文件头
    if (avformat_write_header(ofmt_ctx.get(), nullptr) < 0) {
        jclass listenerCls = env->GetObjectClass(listener);
        jmethodID onError = env->GetMethodID(listenerCls, "onError", "(Ljava/lang/String;)V");
        env->CallVoidMethod(listener, onError, env->NewStringUTF("写入文件头失败"));
        return;
    }

    // 计算总时长用于进度
    int64_t video_duration = ifmt_ctx_v->duration;
    int64_t audio_duration = ifmt_ctx_a->duration;
    int64_t total_duration = video_duration > audio_duration ? video_duration : audio_duration;
    int last_progress = 0;

    // 读取并写入视频帧
    AVPacket pkt;
    // av_init_packet(&pkt); // av_init_packet 在新版本中已弃用, av_packet_alloc() 是更好的选择，但为了最小改动，暂时保留
    bool video_eof = false, audio_eof = false;
    while (!video_eof || !audio_eof) {
        // 选择读取哪个流
        if (!video_eof && (audio_eof ||
                           (av_compare_ts(video_pts, ifmt_ctx_v->streams[video_index_in]->time_base,
                                          audio_pts,
                                          ifmt_ctx_a->streams[audio_index_in]->time_base) <= 0))) {
            // 读视频
            ret = av_read_frame(ifmt_ctx_v, &pkt);
            if (ret >= 0) {
                if(pkt.stream_index == video_index_in) {
                    pkt.stream_index = video_index_out;
                    // 时间戳转换
                    pkt.pts = av_rescale_q_rnd(pkt.pts, ifmt_ctx_v->streams[video_index_in]->time_base,
                                               ofmt_ctx->streams[video_index_out]->time_base,
                                               (AVRounding) (AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
                    pkt.dts = av_rescale_q_rnd(pkt.dts, ifmt_ctx_v->streams[video_index_in]->time_base,
                                               ofmt_ctx->streams[video_index_out]->time_base,
                                               (AVRounding) (AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
                    pkt.duration = av_rescale_q(pkt.duration,
                                                ifmt_ctx_v->streams[video_index_in]->time_base,
                                                ofmt_ctx->streams[video_index_out]->time_base);
                    video_pts = pkt.pts;
                    av_interleaved_write_frame(ofmt_ctx.get(), &pkt);
                    av_packet_unref(&pkt);
                    // 进度回调
                    if (total_duration > 0) {
                        int progress = (int) (100 * video_pts *
                                              av_q2d(ofmt_ctx->streams[video_index_out]->time_base) *
                                              AV_TIME_BASE / total_duration);
                        if (progress > last_progress && progress <= 100) {
                            callback.on_progress(progress);
                            last_progress = progress;
                        }
                    }
                } else {
                    av_packet_unref(&pkt); // 忽略非目标流的包
                }
            } else {
                video_eof = true;
            }
        } else if (!audio_eof) {
            // 读音频
            ret = av_read_frame(ifmt_ctx_a, &pkt);
            if (ret >= 0) {
                if (pkt.stream_index == audio_index_in) {
                    pkt.stream_index = audio_index_out;
                    pkt.pts = av_rescale_q_rnd(pkt.pts, ifmt_ctx_a->streams[audio_index_in]->time_base,
                                               ofmt_ctx->streams[audio_index_out]->time_base,
                                               (AVRounding) (AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
                    pkt.dts = av_rescale_q_rnd(pkt.dts, ifmt_ctx_a->streams[audio_index_in]->time_base,
                                               ofmt_ctx->streams[audio_index_out]->time_base,
                                               (AVRounding) (AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
                    pkt.duration = av_rescale_q(pkt.duration,
                                                ifmt_ctx_a->streams[audio_index_in]->time_base,
                                                ofmt_ctx->streams[audio_index_out]->time_base);
                    audio_pts = pkt.pts;
                    av_interleaved_write_frame(ofmt_ctx.get(), &pkt);
                    av_packet_unref(&pkt);
                    // 进度回调 (可以根据需要保留或移除音频部分的进度计算)
                    if (total_duration > 0) {
                        int progress = (int) (100 * audio_pts *
                                              av_q2d(ofmt_ctx->streams[audio_index_out]->time_base) *
                                              AV_TIME_BASE / total_duration);
                        if (progress > last_progress && progress <= 100) {
                            jclass listenerCls = env->GetObjectClass(listener);
                            jmethodID onProgress = env->GetMethodID(listenerCls, "onProgress", "(I)V");
                            env->CallVoidMethod(listener, onProgress, progress);
                            last_progress = progress;
                        }
                    }
                } else {
                    av_packet_unref(&pkt); // 忽略非目标流的包
                }
            } else {
                audio_eof = true;
            }
        }
    }

    // 写文件尾
    av_write_trailer(ofmt_ctx.get());
    // 完成回调
    callback.on_complete();

    if (ifmt_ctx_v) avformat_close_input(&ifmt_ctx_v);
    if (ifmt_ctx_a) avformat_close_input(&ifmt_ctx_a);
    if (ofmt_ctx && !(ofmt_ctx->oformat->flags & AVFMT_NOFILE)) avio_closep(&ofmt_ctx->pb);
}
