﻿plugins {
    alias(libs.plugins.bilibilias.android.library)
}

android {
    namespace = "com.imcys.bilibilias.core.crash"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("ch.acra:acra-core:5.11.3")
    implementation("ch.acra:acra-dialog:5.11.3")
}
