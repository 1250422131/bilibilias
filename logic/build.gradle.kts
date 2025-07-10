plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.data)
            implementation(projects.core.httpDownloader)

            implementation(libs.decompose)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}