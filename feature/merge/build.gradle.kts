@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.bilias.feature.merge"
}

dependencies {
    implementation(projects.core.network)
    implementation(libs.ffmpeg.ffmpegCommand)
    implementation(libs.ffmpeg.kit.full)
}