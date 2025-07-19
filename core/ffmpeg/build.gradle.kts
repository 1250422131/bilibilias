plugins {
    alias(libs.plugins.bilibilias.kmp.library)
}

kotlin {
    sourceSets {
        jvmMain.dependencies {
            implementation("org.bytedeco:ffmpeg-platform:7.1.1-1.5.12")
            implementation("com.github.pgreze:kotlin-process:1.5.1")
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.ffmpeg"
}