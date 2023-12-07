@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.setting"
}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.common)

    implementation(libs.compose.settings.ui.m3)
    implementation(libs.androidFilePicker)
}