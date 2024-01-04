@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.bilibiliAs.android.hilt)
    alias(libs.plugins.bilibiliAs.android.library.compose)
}

android {
    namespace = "com.bilias.core.testing"
}

dependencies {
    api(kotlin("test"))
    api(libs.androidx.compose.ui.test)
    api(projects.core.model)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(libs.accompanist.testharness)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    testApi(libs.androidx.test.core)
    androidTestApi(libs.androidx.runner)
    androidTestApi(libs.androidx.test.rules)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.truth)

    androidTestApi(libs.androidx.test.espresso.core)
    androidTestApi(libs.androidx.uiautomator)
    androidTestApi(libs.androidx.ui.test.junit4)

    testApi(libs.junit4)

    androidTestUtil(libs.androidx.orchestrator)
}