plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.bilibilias.android.koin)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.imcys.bilibilias.database"
}

dependencies {
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
}