plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.hilt)
    kotlin("kapt")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}