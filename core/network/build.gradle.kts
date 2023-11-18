@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.imcys.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:okdownloader"))
    implementation(project(":common"))

    debugImplementation(libs.monitor)
    releaseImplementation(libs.monitor.no.op)

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.encoding)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.kotlinx.protobuf)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.media3.datasource.okhttp)

    implementation(libs.grpc.okhttp)

    implementation(libs.wireGrpcClient)

    implementation("com.squareup.okhttp3:okhttp-brotli:4.11.0")

    implementation(libs.ffmpeg.kit.full)
    implementation(libs.ffmpeg.ffmpegCommand)
}