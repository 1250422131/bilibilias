#include <jni.h>
#include <renderer.hpp>

using namespace bilias;

extern "C"
JNIEXPORT jlong JNICALL
Java_com_imcys_bilibilias_render_VideoRendererJNI_createRenderer(JNIEnv *env, jobject thiz) {
    auto *renderer = new VideoRenderer();
    return reinterpret_cast<jlong>(renderer);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_VideoRendererJNI_destroyRenderer(JNIEnv *env, jobject thiz,
                                                                  jlong ptr) {
    auto *renderer = reinterpret_cast<VideoRenderer *>(ptr);
    delete renderer;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_VideoRendererJNI_onSurfaceCreated(JNIEnv *env, jobject thiz,
                                                                   jlong ptr) {
    auto *renderer = reinterpret_cast<VideoRenderer *>(ptr);
    renderer->on_surface_created();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_VideoRendererJNI_onSurfaceChanged(JNIEnv *env, jobject thiz,
                                                                   jlong ptr, jint width,
                                                                   jint height) {
    auto *renderer = reinterpret_cast<VideoRenderer *>(ptr);
    renderer->on_surface_changed(width, height);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_VideoRendererJNI_onDrawFrame(JNIEnv *env, jobject thiz, jlong ptr) {
    auto *renderer = reinterpret_cast<VideoRenderer *>(ptr);
    renderer->on_draw_frame();
}