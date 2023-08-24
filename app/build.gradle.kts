plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

ksp {
    arg("ModuleName", project.getName())
}
android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        minSdk = 21
        //noinspecton ExpiredTargetSdkVersion
        targetSdk = 32
        versionCode = 203
        versionName = "2.0.31"
        //multiDexEnabled true
//        def appCenterSecret = getRootProject().getProperties().get("APP_CENTER_SECRET")
//        buildConfigField("String", "APP_CENTER_SECRET", """ + appCenterSecret + """)

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        flavorDimensions(project.name)
    }

    buildTypes {
        debug {
            //混淆
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "@string/app_name_debug")
            resValue("string", "app_channel", "@string/app_channel_debug")
        }

        release {
            //混淆
            isMinifyEnabled = true
            //Zipalign优化
            isZipAlignEnabled = true
            // 移除无用的resource文件
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "@string/app_name_release")
            resValue("string", "app_channel", "@string/app_channel_release")
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = false
        checkReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    dataBinding {
        isEnabled = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.imcys.bilibilias"
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
    buildToolsVersion = "33.0.2"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":common"))
    implementation(project(":model_ffmpeg"))
    implementation(project(":tool_log_export"))

    ksp("com.imcys.deeprecopy:compiler:0.0.1-Alpha-12")

    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    kapt("com.google.dagger:hilt-compiler:2.44")

    ksp("com.github.xiaojinzi123.KComponent:kcomponent-compiler:1.0.0-rc4")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
