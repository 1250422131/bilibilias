# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# 防止混淆FFmpegManger下所有方法和属性
# 保留 FFmpeg 相关类和接口
-keep class com.imcys.bilibilias.ffmpeg.** { *; }

# 保留 JNI 接口方法名
-keep interface com.imcys.bilibilias.ffmpeg.FFmpegManger$FFmpegMergeListener {
    void onProgress(int);
    void onError(java.lang.String);
    void onComplete();
}

# 后续移除，Google未能完成混淆
# https://cs.android.com/androidx/platform/frameworks/support/+/d09d8c618295dcb9e5b554405bc48af244d0a278
-keepclassmembers class androidx.compose.ui.graphics.layer.view.ViewLayerContainer {
    protected void dispatchGetDisplayList();
}
-keepclassmembers class androidx.compose.ui.graphics.layer.view.PlaceholderHardwareCanvas {
    public int drawRenderNode(...);
    public boolean isHardwareAccelerated();
}



-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite* {
   <fields>;
}

-keep class com.baidu.bottom.** { *; }
