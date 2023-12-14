@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.compose)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.imcys.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(projects.core.crash)

    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.cbor)
    api(libs.kotlinx.serialization.protobuf)
    api(libs.kotlinx.datetime)

    api(libs.timber)

    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.kotlin.reflect)

    api(libs.toaster)
    implementation(libs.androidx.startup.runtime)

    api(libs.kotlinx.collections.immutable)
    api(libs.devAppX)

    api(libs.androidx.media3.common)
}
