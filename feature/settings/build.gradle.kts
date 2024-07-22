plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.settings"
}

dependencies {
    implementation(projects.feature.common)
    implementation(projects.core.common)
    implementation(projects.core.datastore)

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.settings)

    testImplementation(projects.core.testing)
    testImplementation(testFixtures(projects.core.datastore))
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)

    androidTestImplementation("com.kaspersky.android-components:kaspresso:1.5.4")
    // Allure support
    androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.5.5")
    // Jetpack Compose support
    androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:1.5.4")
}
