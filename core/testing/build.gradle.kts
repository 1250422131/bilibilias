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
    implementation(projects.core.network)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(libs.accompanist.testharness)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(libs.test.core.ktx)

    implementation(libs.androidx.runner)
    implementation(libs.androidx.rules)

    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.truth)

    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.contrib)
    implementation(libs.androidx.espresso.intents)
    implementation(libs.androidx.espresso.accessibility)
    implementation(libs.androidx.espresso.web)
    implementation(libs.androidx.espresso.idling.concurrent)
    implementation(libs.androidx.espresso.idling.resource)

    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.compose.ui.test)

//    api(libs.junit)
    api(libs.mockk.android)
    api(libs.mockk.agent)
}
