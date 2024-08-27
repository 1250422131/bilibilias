plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.hilt)
}

android {
    namespace = "com.imcys.bilibilias.sync"
    defaultConfig {
        testInstrumentationRunner = "com.imcys.bilibilias.core.testing.AsTestRunner"
    }
}

dependencies {
    ksp(libs.hilt.ext.compiler)

    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.work.runtime)
    implementation(libs.hilt.ext.work)
    implementation(projects.core.analytics)
    implementation(projects.core.data)
    implementation(projects.core.network)

    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(projects.core.testing)
}