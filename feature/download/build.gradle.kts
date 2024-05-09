import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.bilibilias.android.feature)
    alias(libs.plugins.bilibilias.android.library.compose)
    alias(libs.plugins.bilibilias.android.library.jacoco)
}

android {
    namespace = "com.imcys.bilibilias.feature.download"

//    packaging {
//        resources {
//            pickFirsts += "lib/x86/libc++_shared.so"
//            pickFirsts += "lib/x86_64/libc++_shared.so"
//            pickFirsts += "lib/armeabi-v7a/libc++_shared.so"
//            pickFirsts += "lib/arm64-v8a/libc++_shared.so"
//        }
//    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.download)
    implementation(projects.core.database)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
