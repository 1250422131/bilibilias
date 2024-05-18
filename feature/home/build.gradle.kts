plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.logic.components)
}

android {
    namespace = "com.imcys.bilibilias.feature.home"
}

dependencies {
    implementation(projects.feature.common)

    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.banner)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
