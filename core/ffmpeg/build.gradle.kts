plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.ffmpeg"
    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
//            excludes += "/**/*.kotlin_builtins" // (example)
        }
    }
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.ffmpeg.kit.full)
}
