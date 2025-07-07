plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)

            api(libs.decompose)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.logic"
}