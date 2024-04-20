﻿plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.common"
}

dependencies {
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.collections.immutable)
    api("com.squareup.okio:okio:3.9.0")
    api(libs.napier)
    api(libs.devappx)
    implementation(kotlin("reflect"))
    implementation(libs.androidx.preference.ktx)
}