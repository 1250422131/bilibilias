plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.httpDownloader)

            implementation(projects.core.datasource)

            implementation(libs.decompose)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}