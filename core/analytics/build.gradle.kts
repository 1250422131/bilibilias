plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(libs.umeng.common)
    implementation(libs.umeng.asms)
    implementation(libs.umeng.abtest)
}
