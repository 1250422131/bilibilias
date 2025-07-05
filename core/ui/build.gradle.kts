plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.imcys.bilibilias.ui"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
}