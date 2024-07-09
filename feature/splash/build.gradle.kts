plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.splash"
}

dependencies {
    implementation(projects.core.datastore)

    testImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
