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
            srcDirs("src/main/proto")
        }
    }
}

dependencies {
    implementation("com.squareup.wire:wire-runtime:5.0.0")
}
