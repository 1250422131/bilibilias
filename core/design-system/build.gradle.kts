@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.imcys.design.system"
}

dependencies {
    api(libs.ui)
    api(libs.ui.graphics)
    api(libs.ui.tooling.preview)
    api(libs.ui.tooling)
    api(libs.androidx.material3)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.coil.compose)
    implementation(project(":core:common"))
}