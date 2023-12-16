@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.feature)
    alias(libs.plugins.bilibili.android.compose)
}

android {
    namespace = "com.imcys.tool"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.common)
    /**
     * jxl库
     * 直接生成excel文件时采用
     */
    implementation(libs.jxl)

    implementation(libs.androidFilePicker)
}
