@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.home"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.zhujiang.banner)
}