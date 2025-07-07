plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.bilibilias.compose)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.compose.ui.tooling.preview)
            api(libs.androidx.compose.ui.tooling)
        }
        commonMain.dependencies {
            api(libs.androidx.annotation)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.ui.preview"
}