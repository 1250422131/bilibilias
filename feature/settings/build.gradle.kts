plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.multiplatform.decompose)
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
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
