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

    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.cbor)

    api(libs.timber)

    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.kotlin.reflect)

    api("com.github.getActivity:Toaster:12.6")
    implementation(libs.androidx.startup.runtime)

    api(libs.kotlinx.collections.immutable)
}
