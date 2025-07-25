plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.data)
            implementation(projects.core.ffmpeg)
            implementation(projects.core.mediaCache)
            implementation(projects.core.httpDownloader)

            implementation(libs.decompose)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}