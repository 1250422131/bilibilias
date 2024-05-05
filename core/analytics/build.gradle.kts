plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(libs.appcenter.crashes)
    implementation(libs.appcenter.analytics)
    implementation(libs.appcenter.distribute)
}
