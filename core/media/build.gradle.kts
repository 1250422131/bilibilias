plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.hilt)
    alias(libs.plugins.bilibilias.android.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.core.media"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
}
