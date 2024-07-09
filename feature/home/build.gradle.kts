plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.home"
}

dependencies {
    implementation(projects.feature.common)

    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.banner)

    testImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
