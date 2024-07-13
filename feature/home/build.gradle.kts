plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.home"
    testFixtures.enable = true
}

dependencies {
    implementation(projects.feature.common)

    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.banner)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)
    testImplementation(projects.core.screenshotTesting)
    testImplementation(testFixtures(projects.core.data))
    testImplementation(testFixtures(projects.core.analytics))
    testDemoImplementation(libs.roborazzi)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(testFixtures(projects.core.model))
}
