plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.multiplatform.decompose)
}

android {
    namespace = "com.imbys.bilibilias.feature.authorspace"
}

dependencies {
    implementation(projects.core.network)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.common)
}
