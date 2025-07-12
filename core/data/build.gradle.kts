plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.datasource)
            api(projects.core.model)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.data"
}