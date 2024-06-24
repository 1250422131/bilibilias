plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.domain"
}

dependencies {
    api(projects.core.model)
    api(projects.core.network)

//    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    testImplementation(projects.core.testing)
    testImplementation(testFixtures(projects.core.data))
}
