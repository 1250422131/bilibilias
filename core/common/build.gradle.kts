plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.common"
}

dependencies {
    api(libs.kotlinx.collections.immutable)
    api(libs.androidx.collection)
    api(libs.okio)
    api(libs.napier)
    api(libs.devappx)

    testImplementation(kotlin("test"))
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    implementation("com.github.albfernandez:juniversalchardet:2.5.0")
}
