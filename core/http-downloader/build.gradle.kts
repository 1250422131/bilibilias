plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ktorClient)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.http.downloader"
}