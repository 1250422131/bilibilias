@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.tool"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    // implementation(libs.androidx.lifecycle.runtime.compose)
    /**
     * jxl库
     * 直接生成excel文件时采用
     */
    implementation(libs.jxl)
}