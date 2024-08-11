plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.analytics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.video)
    implementation(libs.coil.svg)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(testFixtures(projects.core.model))
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}
