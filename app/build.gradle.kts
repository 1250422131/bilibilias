@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.application)
    alias(libs.plugins.bilibili.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        applicationId = AndroidConfigConventions.BilibiliAS.APPLICATION_ID
        versionCode = AndroidConfigConventions.BilibiliAS.VERSION_CODE
        versionName = AndroidConfigConventions.BilibiliAS.VERSION_NAME

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":common"))

    implementation(project(":core:design-system"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":feature:tool"))

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.material3)

    ksp(libs.kcomponent.compiler)
    implementation(libs.material)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation.material)
    // implementation(libs.compose.material3.window.size)
    // implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)

    /**
     * banner
     */
    implementation(libs.zhujiang.banner)

    /**
     * 网络图片加载库
     */
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.kotlinx.serialization.cbor)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    /**
     * 下载
     */
    implementation(libs.androidx.xfetch2)
    implementation(libs.compose.settings.ui.m3)

    /**
     * ffmpeg
     */
    implementation(libs.ffmpeg.kit.full)
    /**
     * protobuf
     */
    implementation(libs.protobuf.kotlin)
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
    implementation(libs.androidx.media3.datasource.okhttp)
    implementation(libs.androidx.media3.datasource.cronet)

    implementation(libs.androidx.media3.session) // [Required] MediaSession Extension dependency
    implementation(libs.androidx.media3.decoder)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.cronet.embedded) {
        exclude(group = "com.google.protobuf")
    }

    implementation(libs.timber)
}
