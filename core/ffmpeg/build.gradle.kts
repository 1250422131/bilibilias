plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.ffmpegVerification)
}

android {
    namespace = "com.imcys.bilibilias.ffmpeg"

    defaultConfig {

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a","x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags("-DCMAKE_CXX_FLAGS=-frtti -fexceptions -Wno-deprecated-declaration -std=c++23")
            }
        }
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
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    ndkVersion = "27.0.12077973"
}

dependencies {
    implementation(project(":core:common"))
}