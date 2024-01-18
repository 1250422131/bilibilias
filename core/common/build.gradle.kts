@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.bilibiliAs.android.hilt)
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
    api(libs.napier)

    implementation(libs.kotlin.reflect)

    api(libs.toaster)
    implementation(libs.androidx.startup.runtime)

    api(libs.kotlinx.collections.immutable)
    api(libs.devAppX)

    api(libs.androidx.media3.common)
    testImplementation(projects.core.testing)
}
