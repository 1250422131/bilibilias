@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.authentication"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:design-system"))
    implementation(project(":core:datastore"))
}
