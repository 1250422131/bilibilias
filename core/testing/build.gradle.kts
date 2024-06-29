plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    kotlin("plugin.power-assert")
}

android {
    namespace = "com.imcys.bilibilias.core.testing"
}

dependencies {
    api(kotlin("test", "2.0.0"))
    api(libs.androidx.compose.ui.test)
    api(projects.core.analytics)
    api(projects.core.data)
    api(projects.core.model)

    debugApi(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(testFixtures(projects.core.data))
    implementation(testFixtures(projects.core.datastore))
}