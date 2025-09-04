plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api("androidx.navigation3:navigation3-runtime:1.0.0-alpha08")
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.androidx.savedstate.compose)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.navigation"
}