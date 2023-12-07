@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.imcys.datastore"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.mmkv)
    implementation(libs.fastkv)
    implementation(libs.androidx.datastore)
}
