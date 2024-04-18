﻿plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.ffmpeg"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.ffmpegcommand)
}
