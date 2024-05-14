plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.feature.splash"
}

dependencies {
    implementation(projects.core.datastore)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
