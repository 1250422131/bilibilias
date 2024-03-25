plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.network"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

//    implementation(libs.coil.kt)
//    implementation(libs.coil.kt.svg)
    implementation(libs.kotlinx.serialization.json)

//    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}