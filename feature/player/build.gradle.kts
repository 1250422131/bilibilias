plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.multiplatform.decompose)
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
