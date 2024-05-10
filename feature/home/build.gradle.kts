plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.feature.home"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.coil.compose)
    implementation(libs.banner)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
