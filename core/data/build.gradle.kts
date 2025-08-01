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
            api(projects.core.datastore)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.data"
}