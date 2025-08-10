#pragma once

#include "traits.hpp"
#include <jni.h>
#include <exception>
#include <string_view>

namespace bilias {

    inline auto throw_jni_exception(JNIEnv *env, std::string_view msg) -> void {
        auto ex_class = env->FindClass("com/imcys/bilibilias/NativeException");
        if (ex_class != nullptr) {
            env->ThrowNew(ex_class, msg.data());
        }
    }

    template<typename Fn, typename R = std::invoke_result_t<Fn>>
    auto jni_safe_call_r(JNIEnv *env, Fn &&fn, R default_value = R{}) -> R {
        try {
            return fn();
        } catch (const std::exception &e) {
            throw_jni_exception(env, e.what());
        } catch (...) {
            throw_jni_exception(env, "unknown exception");
        }
        return default_value;
    }

    template<typename Fn>
    auto jni_safe_call(JNIEnv *env, Fn &&fn) -> void {
        try {
            return fn();
        } catch (const std::exception &e) {
            throw_jni_exception(env, e.what());
        } catch (...) {
            throw_jni_exception(env, "unknown exception");
        }
    }
}

#define JNI_SAFE_CALL_BEGIN(env) \
    try {

#define JNI_SAFE_CALL_END(env) \
    } catch (const std::exception &e) { \
         ::bilias::throw_jni_exception(env, e.what()); \
    } catch (...) {            \
         ::bilias::throw_jni_exception(env, "unknown exception"); \
    }
