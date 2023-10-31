@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.compose)
    alias(libs.plugins.bilibili.android.hilt)
}

android {
    namespace = "com.imcys.tool"
}

dependencies {
    implementation(project(":core:design-system"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    /**
     * jxl库
     * 直接生成excel文件时采用
     */
    implementation(libs.jxl)
}
