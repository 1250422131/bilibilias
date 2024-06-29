plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    alias(libs.plugins.bilibilias.android.room)
    alias(libs.plugins.bilibilias.multiplatform.sqlLin)
}

android {
    namespace = "com.imcys.bilibilias.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(projects.core.testing)
}