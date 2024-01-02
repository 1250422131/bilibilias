@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.kotlin.serialization)
    id("com.squareup.wire")
}

android {
    namespace = "com.imcys.model"
}

wire {
    kotlin {
        rpcRole = "client"
        rpcCallStyle = "suspending"
        exclusive = true
    }
    sourcePath {
        srcDirs("src/main/proto")
    }
}
dependencies {
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.wireRuntime)
    implementation(libs.wireGrpcClient)
}
