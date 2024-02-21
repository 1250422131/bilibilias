@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.imcys.model"
}


dependencies {
    implementation(libs.kotlinx.serialization.json)
}
