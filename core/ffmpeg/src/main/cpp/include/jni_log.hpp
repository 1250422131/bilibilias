#pragma once
#include <cstring>
#include <format>
#include <string_view>
#include <android/log.h>

#define BILIAS_FILE_NAME (strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__)
#define LOG_TAG "BILIAS NATIVE"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "[%s:%d] " __VA_ARGS__, BILIAS_FILE_NAME, __LINE__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "[%s:%d] " __VA_ARGS__, BILIAS_FILE_NAME, __LINE__)

namespace bilias {

    template<typename ...Args>
    auto log(android_LogPriority level, std::format_string<Args...> format_str, Args &&...args) -> void {
        auto str = std::format(format_str, std::forward<Args>(args)...);
        __android_log_print(level, LOG_TAG, "%s", str.c_str());
    }

    template<typename ...Args>
    auto log_d(std::format_string<Args...> format_str, Args &&...args) -> void {
        log(ANDROID_LOG_DEBUG, format_str, std::forward<Args>(args)...);
    }

    template<typename ...Args>
    auto log_i(std::format_string<Args...> format_str, Args &&...args) -> void {
        log(ANDROID_LOG_INFO, format_str, std::forward<Args>(args)...);
    }

    template<typename ...Args>
    auto log_w(std::format_string<Args...>  format_str, Args &&...args) -> void {
        log(ANDROID_LOG_WARN, format_str, std::forward<Args>(args)...);
    }

    template<typename ...Args>
    auto log_e(std::format_string<Args...>  format_str, Args &&...args) -> void {
        log(ANDROID_LOG_ERROR, format_str, std::forward<Args>(args)...);
    }
}
