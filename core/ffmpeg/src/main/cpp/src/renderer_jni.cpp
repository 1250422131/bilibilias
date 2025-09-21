#include <jni.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <player_controller.hpp>
#include <jni_util.hpp>


extern "C"
JNIEXPORT jlong JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_createPlayer(JNIEnv *env, jclass clazz) {
    auto *controller = new bilias::PlayerController();
    return reinterpret_cast<jlong>(controller);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_initPlayer(JNIEnv *env, jclass clazz,
                                                          jlong player_ptr, jobject surface, jint fd) {
    bilias::jni_safe_call(env, [&] {
        auto *controller = reinterpret_cast<bilias::PlayerController *>(player_ptr);
        auto *window = ANativeWindow_fromSurface(env, surface);
        controller->initialize(window, fd);
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_setPlayerViewports(JNIEnv *env, jclass clazz,
                                                                  jlong player_ptr, jint width,
                                                                  jint height) {
    bilias::jni_safe_call(env, [&] {
        auto *controller = reinterpret_cast<bilias::PlayerController *>(player_ptr);
        controller->set_viewport(width, height);
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_play(JNIEnv *env, jclass clazz, jlong player_ptr) {
    bilias::jni_safe_call(env, [&] {
        auto *controller = reinterpret_cast<bilias::PlayerController *>(player_ptr);
        controller->play();
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_pause(JNIEnv *env, jclass clazz, jlong player_ptr) {
    bilias::jni_safe_call(env, [&] {
        auto *controller = reinterpret_cast<bilias::PlayerController *>(player_ptr);
        controller->pause();
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_stop(JNIEnv *env, jclass clazz, jlong player_ptr) {
    bilias::jni_safe_call(env, [&] {
        auto *controller = reinterpret_cast<bilias::PlayerController *>(player_ptr);
        controller->stop();
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_imcys_bilibilias_render_PlayerJNI_releasePlayer(JNIEnv *env, jclass clazz,
                                                             jlong player_ptr) {
    bilias::jni_safe_call(env, [&] {
        auto *controller = reinterpret_cast<bilias::PlayerController *>(player_ptr);
        delete controller;
    });
}
