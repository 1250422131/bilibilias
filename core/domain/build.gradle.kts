plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            commonMain.dependencies {
                api(projects.core.data)
                api(projects.core.model)

                implementation(projects.core.httpDownloader)
                implementation(projects.core.mediaCache)
            }
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.domain"
}