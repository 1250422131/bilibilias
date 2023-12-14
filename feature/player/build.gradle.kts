@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.player"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.common)
    /**
     * gsy 播放器
     */
    implementation(libs.gsyVideoPlayer.java)
    /**
     * ExoPlayer模式
     */
    implementation(libs.gsyVideoPlayer.exo2)

    /**
     * 视频网络请求
     */
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.transformer)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.decoder)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.androidx.media3.datasource.okhttp)
    implementation(libs.androidx.media3.exoplayer.workmanager)
    implementation(libs.androidx.media3.extractor)
    implementation(libs.androidx.media3.database)

    implementation(libs.accompanist.navigation.material)

    implementation(libs.androidx.paging.compose)

    implementation(libs.ffmpeg.kit.full)
    implementation(libs.ffmpeg.ffmpegCommand)
}