plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.datastore"
    testFixtures.enable = true
}

dependencies {
    api(libs.androidx.dataStore)
    api(projects.core.datastoreProto)
    api(projects.core.model)

    implementation(projects.core.common)

    kspTestFixtures(libs.hilt.compiler)
    testFixturesImplementation(libs.hilt.android.testing)
    testFixturesImplementation(projects.core.common)
    testFixturesImplementation(projects.core.datastore)
}