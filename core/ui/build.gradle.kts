@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:design-system"))
    api(libs.ui)
    api(libs.ui.graphics)
    api(libs.ui.tooling)
    api(libs.ui.tooling.preview)
    // api(libs.compose.material3)
}