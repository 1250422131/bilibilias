plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api("androidx.navigation3:navigation3-runtime:1.0.0-alpha08")
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.savedstate.compose)
            implementation(libs.kotlinx.serialization.core)

            implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.9.3")
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.navigation"
}