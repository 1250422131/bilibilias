plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            implementation(projects.core.ffmpeg)
            implementation(projects.core.datastore)
            implementation(projects.core.httpDownloader)

            // todo implementation
            api(libs.flowredux)

            implementation(libs.koin.compose.viewmodel)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}