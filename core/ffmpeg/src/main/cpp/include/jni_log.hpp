#pragma once
#include <cstring>
#include <android/log.h>

#define BILIAS_FILE_NAME (strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__)
#define LOG_TAG "BILIAS NATIVE"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "[%s:%d] " __VA_ARGS__, BILIAS_FILE_NAME, __LINE__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "[%s:%d] " __VA_ARGS__, BILIAS_FILE_NAME, __LINE__)