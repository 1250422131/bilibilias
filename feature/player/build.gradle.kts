@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.player"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:common"))
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
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.transformer)
    implementation(libs.androidx.media3.datasource.cronet)

    implementation(libs.androidx.media3.session) // [Required] MediaSession Extension dependency
    implementation(libs.androidx.media3.decoder)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.cronet.embedded) {
        exclude(group = "com.google.protobuf")
    }


    implementation(libs.accompanist.navigation.material)

}