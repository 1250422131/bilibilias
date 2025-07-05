plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.imcys.bilibilias.common"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(project(":core:ui"))
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
}