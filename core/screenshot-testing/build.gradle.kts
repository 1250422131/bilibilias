plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.core.screenshottesting"
}
dependencies {
    api(libs.bundles.androidx.compose.ui.test)
    api(libs.roborazzi)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.activity.compose)
    implementation(libs.robolectric)
    implementation(projects.core.designsystem)
}