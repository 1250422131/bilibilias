plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.player"
}

dependencies {
    implementation(projects.feature.common)
    implementation(projects.core.network)
    implementation(projects.core.database)

    implementation(libs.androidx.media3.datasource.okhttp)

    implementation(libs.gsyvideoplayer.java)
    implementation(libs.gsyvideoplayer.exo2)

    implementation(libs.akdanmaku)

    testImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
