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

            implementation(libs.flowredux)
            implementation(libs.lifecycle.coroutines)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}