plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}
ksp {
    arg("compose-destinations.moduleName", "user")
}
android {
    namespace = "com.imcys.bilibilias.feature.user"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
