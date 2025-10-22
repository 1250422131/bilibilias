plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.koin)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.imcys.bilibilias.network"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    api(libs.ktor.client.core)
    // api(libs.ktor.client.okhttp)
    api(libs.ktor.client.cio)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.serialization.kotlinx.protobuf)
    api(libs.ktor.client.logging)

}