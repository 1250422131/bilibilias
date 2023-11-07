@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    id("kotlinx-serialization")
}

android {
    namespace = "com.imcys.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
