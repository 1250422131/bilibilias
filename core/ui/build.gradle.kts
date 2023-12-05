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

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    // api(libs.androidx.metrics)
    // api(libs.androidx.tracing.ktx)

    debugApi(libs.androidx.compose.ui.tooling)

    // implementation(projects.core.analytics)
    implementation(project(":core:design-system"))
    // implementation(projects.core.domain)
    implementation(project(":core:model"))
    implementation(libs.androidx.activity.compose)
    // implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    api("com.github.razaghimahdi:Compose-Loading-Dots:1.2.3")
}