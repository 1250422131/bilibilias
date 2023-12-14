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
    implementation(libs.acra.http)
    implementation(libs.acra.mail)
    implementation(libs.acra.core)
    implementation(libs.acra.dialog)
    implementation(libs.acra.notification)
    implementation(libs.acra.toast)
    implementation(libs.acra.limiter)
    implementation(libs.acra.scheduler)
}