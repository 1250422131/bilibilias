@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    id("kotlinx-serialization")
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
