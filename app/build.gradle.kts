plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.koin)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.kotlin.plugin.serialization)

}

android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        targetSdk = 36
        applicationId = "com.imcys.bilibilias"
        versionCode = 300
        versionName = "3.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {


    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ffmpeg"))


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.config)
    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.perf)

    // 彩带
    implementation(libs.konfetti.compose)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
