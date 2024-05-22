plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.hilt)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.imcys.bilibilias.common"

    dataBinding {
        enable = true
    }
}

dependencies {
    implementation(project(":core:network"))

    /**
     * jxl库
     * 直接生成excel文件时采用
     */
    api(libs.jxl)

    /**
     * 底部对话框库
     * 为AS专门打造适配的对话框库
     */
    api(libs.asBottomDialog)

    /**
     * RxFFmpeg
     * 支持视频合并等操作
     */
    api(libs.rxFFmpeg)

    api(libs.appcenter.distribute)
    api(libs.appcenter.analytics)
    api(libs.appcenter.crashes)

    /**
     * room
     * 本地化数据库
     */
    api(libs.room.runtime)
    api(libs.room.ktx)
    ksp(libs.room.compiler)

    // 饺子播放器
    api(libs.jiaozivideoplayer)

    // lottie动画库
    api(libs.lottie)

    api(libs.banner)

    api(libs.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
}
