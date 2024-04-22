﻿plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    id("app.cash.molecule")
}

android {
    namespace = "com.imcys.bilibilias.feature.download"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.download)

    implementation(libs.flowmvi.android)
    implementation(libs.flowmvi.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
