plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            commonMain.dependencies {
                implementation(projects.core.model)
                implementation(projects.core.common)

                implementation(projects.core.httpDownloader)
            }
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.datastore"
}