#include <jni.h>
#include <string>

extern "C" {
#include "libavutil/avutil.h"
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