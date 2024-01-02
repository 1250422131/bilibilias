@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.feature)
    alias(libs.plugins.bilibiliAs.android.library.compose)
}

android {
    namespace = "com.imcys.userspace"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.common)

    implementation(libs.androidx.paging.compose)
}