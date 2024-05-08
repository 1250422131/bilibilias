plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.feature.settings"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)

    implementation(libs.compose.settings)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
