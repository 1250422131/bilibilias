plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.core.designsystem"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
//    lintPublish(projects.lint)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)

    implementation(libs.coil.compose)

    testImplementation(libs.androidx.compose.ui.test)
//    testImplementation(libs.accompanist.testharness)
//    testImplementation(libs.hilt.android.testing)
//    testImplementation(libs.robolectric)
//    testImplementation(libs.roborazzi)
//    testImplementation(projects.core.screenshotTesting)
    testImplementation(projects.core.testing)

    api(libs.sonner)
}
