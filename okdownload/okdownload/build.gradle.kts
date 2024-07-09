plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.hilt)
    alias(libs.plugins.bilibilias.android.jacoco)
}

android {
    namespace = "com.liulishuo.okdownload"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.androidx.annotation)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.startup.runtime)
    testImplementation(libs.junit)
}
