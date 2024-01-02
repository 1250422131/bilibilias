@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.bilibiliAs.android.hilt)
}

android {
    namespace = "com.bilias.core.domain"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(project(":core:common"))
}