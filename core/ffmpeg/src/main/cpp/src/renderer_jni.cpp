#include <jni.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <video_renderer.hpp>


extern "C"
JNIEXPORT jlong JNICALL
Java_com_imcys_bilibilias_render_RenderUtilJNI_setSurface(JNIEnv *env, jclass clazz,
                                                          jobject surface) {
    return reinterpret_cast<jlong>(ANativeWindow_fromSurface(env, surface));
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_imcys_bilibilias_render_RenderUtilJNI_createRenderer(JNIEnv *env, jclass clazz) {
    auto *renderer = new bilias::VideoRenderer();
    return reinterpret_cast<jlong>(renderer);
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_imcys_bilibilias_render_RenderUtilJNI_initRenderer(JNIEnv *env, jclass clazz,
                                                            jlong renderer, jobject surface, jint fd) {
    auto *r = reinterpret_cast<bilias::VideoRenderer *>(renderer);
    auto *s = ANativeWindow_fromSurface(env, surface);
    return r->initialize(s, fd);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_RenderUtilJNI_setRendererViewports(JNIEnv *env, jclass clazz,
                                                                    jlong renderer, jint width,
                                                                    jint height) {
    auto *r = reinterpret_cast<bilias::VideoRenderer *>(renderer);
    r->set_viewport(width, height);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_RenderUtilJNI_releaseRenderer(JNIEnv *env, jclass clazz,
                                                               jlong renderer) {
    auto *r = reinterpret_cast<bilias::VideoRenderer *>(renderer);
    delete r;
}
