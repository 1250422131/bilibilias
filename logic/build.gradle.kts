plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            implementation(projects.core.ffmpeg)
            implementation(projects.core.mediaCache)
            implementation(projects.core.httpDownloader)

            implementation(libs.decompose)

            implementation(libs.flowredux)
            implementation("com.arkivanov.essenty:lifecycle-coroutines:2.5.0")
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}