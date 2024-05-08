plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}

android {
    namespace = "com.sockmagic.login"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.qrose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
