@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.room)
}

android {
    namespace = "com.imcys.bilibilias.okdownload"
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.okio)
    implementation(libs.commons.codec)
}
