@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.authentication"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.designsystem)
    implementation(projects.core.datastore)

    implementation(libs.zxing.android.embedded)
    implementation(libs.qrose)
}
