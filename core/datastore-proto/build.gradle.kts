@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    id("com.squareup.wire")
}

android {
    namespace = "com.bilias.datastore.proto"
}
wire {
    kotlin {
        sourcePath {
            srcDirs("src/main/proto")
        }
    }
}
dependencies {
    implementation(libs.wireRuntime)
    implementation(libs.wireGrpcClient)
}
