plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.multiplatform.decompose)
}

android {
    namespace = "com.imcys.bilibilias.feature.login"
}

dependencies {
    implementation(projects.feature.common)

    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.qrose)

    testImplementation(projects.core.testing)
    testImplementation(testFixtures(projects.core.data))

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(testFixtures(projects.core.model))
}
