@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.feature)
    alias(libs.plugins.bilibiliAs.android.library.compose)
}

android {
    namespace = "com.imcys.biliAs.feature.tool"
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }
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
    testImplementation(projects.core.testing)
    androidTestImplementation(projects.core.testing)
}
