plugins {
    alias(libs.plugins.bilibilias.kmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.media3.transformer)
        }
        commonMain.dependencies {
            implementation(projects.core.common)
        }
        jvmMain.dependencies {
//            implementation(libs.ffmpeg.platform)
        }
    }
}

android {
    defaultConfig {
        ndk {
            abiFilters += "arm64-v8a"
        }
    }
    namespace = "com.imcys.bilibilias.core.ffmpeg"
}