plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.multiplatform.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.user"
}

dependencies {
    implementation(projects.feature.common)

    implementation(projects.core.network)
    implementation(projects.core.datastore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
