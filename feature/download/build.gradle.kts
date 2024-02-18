@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.feature)
    alias(libs.plugins.bilibiliAs.android.library.compose)
}

android {
    namespace = "com.bilias.feature.download"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.okdownloader)
    implementation(projects.core.model)
    implementation(projects.core.common)
}
