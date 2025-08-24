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

            api(libs.flowredux)
            implementation(libs.lifecycle.coroutines)

            implementation("io.github.vinceglb:filekit-core:0.10.0")
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}