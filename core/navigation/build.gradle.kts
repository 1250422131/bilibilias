plugins {
    alias(libs.plugins.bilibilias.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api("androidx.navigation3:navigation3-runtime:1.0.0-alpha08")
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.savedstate.compose)
            implementation(libs.kotlinx.serialization.core)

            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.navigation"
}