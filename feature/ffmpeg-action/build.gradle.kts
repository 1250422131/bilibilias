plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.ffmpegaction"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ffmpeg)

    implementation(libs.androidx.activity.compose)
    implementation(libs.filekit.compose)

    implementation(libs.urlencoder)

    testImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
