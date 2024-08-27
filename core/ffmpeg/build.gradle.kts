plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.ffmpeg"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        testInstrumentationRunner = "com.imcys.bilibilias.core.testing.AsTestRunner"
    }
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.ffmpeg.kit.full)

    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(projects.core.testing)
}
