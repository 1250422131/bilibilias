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

            implementation(libs.decompose)
            // todo implementation
            api(libs.flowredux)

            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.2")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")

            implementation(libs.koin.compose.viewmodel)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}