plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.imcys.bilibilias.core.player"
}

dependencies {
    implementation(projects.core.network)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.datasource.okhttp)

    implementation(libs.gsyvideoplayer.java)
    implementation(libs.gsyvideoplayer.exo2)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.akdanmaku)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
