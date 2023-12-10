@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
}

android {
    namespace = "com.bilias.crash"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.annotation.jvm)
}