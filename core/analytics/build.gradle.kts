plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.analytics"
    testFixtures.enable = true
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(libs.umeng.common)
    implementation(libs.umeng.asms)
    implementation(libs.umeng.abtest)
}
