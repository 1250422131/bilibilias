@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.bilias.core.domain"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
}