plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.library.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.datastore"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
    testFixtures.enable = true
}

dependencies {
    api(libs.androidx.datastore)
//    api(libs.androidx.dataStore.core)
    api(projects.core.datastoreProto)
    api(projects.core.model)

    implementation(projects.core.common)

    kspTestFixtures(libs.hilt.compiler)
    testFixturesImplementation(libs.hilt.android.testing)
    testFixturesImplementation(projects.core.common)
    testFixturesImplementation(projects.core.datastore)
}