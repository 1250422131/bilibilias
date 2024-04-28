plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.network"
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        create("nonMinifiedRelease") {
        }
        create("benchmarkRelease") {
        }
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    implementation(projects.core.datastore)

    implementation(libs.coil.kt)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)
    implementation(libs.coil.video)

    implementation(libs.kotlinx.atomicfu)
//    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.androidx.compose.runtime)

    debugImplementation(libs.monitor)
    releaseImplementation(libs.monitor.no.op)

    implementation(libs.okhttp.brotli)
}
