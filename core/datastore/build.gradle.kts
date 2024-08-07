plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.imcys.bilibilias.core.datastore"
    testFixtures.enable = true
}

dependencies {
    api(libs.androidx.dataStore)
    api(projects.core.model)

    implementation(projects.core.common)

    implementation(libs.kotlinx.serialization.protobuf)

    kspTestFixtures(libs.hilt.compiler)
    testFixturesImplementation(libs.hilt.android.testing)
    testFixturesImplementation(projects.core.common)
    testFixturesImplementation(projects.core.datastore)

    testImplementation(libs.kotlinx.coroutines.test)
}
