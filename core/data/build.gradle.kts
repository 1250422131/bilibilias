plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    alias(libs.plugins.bilibilias.android.testoptions)
}

android {
    namespace = "com.imcys.bilibilias.core.data"
    testFixtures.enable = true
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.network)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)

    testImplementation(testFixtures(projects.core.datastore))

    kspTestFixtures(libs.hilt.compiler)
    testFixturesImplementation(libs.hilt.android.testing)
}