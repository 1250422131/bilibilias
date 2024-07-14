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

    prodImplementation(libs.umeng.common)
    prodImplementation(libs.umeng.asms)
    prodImplementation(libs.umeng.abtest)
}
