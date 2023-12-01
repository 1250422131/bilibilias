@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.imcys.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.fastkv)
    implementation(libs.mmkv)
    implementation(project(mapOf("path" to ":core:model")))
}
