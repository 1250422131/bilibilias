plugins {
    alias(libs.plugins.bilibilias.kmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.media3.transformer)
            implementation(libs.ffmpeg.kit.full.gpl)
        }
        commonMain.dependencies {
            implementation(projects.core.common)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.ffmpeg"
}