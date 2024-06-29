plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.hilt)
}

android {
    namespace = "com.imcys.bilibilias.feature.common"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)
    implementation(libs.molecule)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
