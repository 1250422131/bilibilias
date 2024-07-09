plugins {
    alias(libs.plugins.bilibilias.jvm.library)
    alias(libs.plugins.bilibilias.hilt)
}

dependencies {
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.collections.immutable)
    api(libs.okio)
    api(libs.napier)
    api(libs.devappx)
    api(libs.toaster)
    implementation(libs.kotlin.reflect)

    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
//    testImplementation(libs.turbine)
}
