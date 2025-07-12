plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
    }
}

android {
    namespace = "com.imcys.bilibilias.core.model"
}