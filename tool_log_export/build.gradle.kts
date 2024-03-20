@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.imcys.bilibilias.tool_log_export"

    dataBinding {
        enable = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.constraintlayout)
    ksp(libs.kcomponent.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}