plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.data)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.http.cache"
}