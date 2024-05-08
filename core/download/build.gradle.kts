plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.download"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.core.ffmpeg)
    implementation(projects.core.datastore)

    implementation(libs.kotlinx.datetime)

    implementation(projects.okdownload.okdownload)

    implementation("com.lazygeniouz:dfc:1.0.8")

    implementation(libs.okhttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
