plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.download"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.core.common)
    implementation(projects.core.ffmpeg)
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(libs.kotlinx.datetime)

    implementation(projects.okdownload.okdownload)

    implementation(libs.dfc)

    implementation(libs.urlencoder)
}
