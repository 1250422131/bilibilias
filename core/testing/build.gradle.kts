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

    androidTestApi(libs.test.core.ktx)
    testApi(libs.test.core.ktx)

    androidTestApi(libs.androidx.runner)
    androidTestApi(libs.androidx.rules)

    androidTestApi(libs.androidx.junit.ktx)
    androidTestApi(libs.androidx.truth)

    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(libs.androidx.espresso.contrib)
    androidTestApi(libs.androidx.espresso.intents)
    androidTestApi(libs.androidx.espresso.accessibility)
    androidTestApi(libs.androidx.espresso.web)
    androidTestApi(libs.androidx.espresso.idling.concurrent)
    androidTestApi(libs.androidx.espresso.idling.resource)

    androidTestApi(libs.androidx.uiautomator)
    androidTestApi(libs.androidx.compose.ui.test)

    testApi(libs.junit)
    testApi(libs.mockito.core)
    testApi(libs.mockito.kotlin)
    testApi(libs.mockk)
}