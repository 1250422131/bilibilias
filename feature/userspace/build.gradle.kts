@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.userspace"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.model)

    implementation(libs.androidx.paging.compose)
}