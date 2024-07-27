plugins {
    alias(libs.plugins.bilibilias.android.library)
    id("com.squareup.wire")
}

android {
    namespace = "com.imcys.bilibilias.core.datastore.proto"
}

wire {
    kotlin {
        sourcePath {
            srcDir(files("src/main/proto"))
        }
    }
}

dependencies {
    implementation(libs.wire.runtime)
}
