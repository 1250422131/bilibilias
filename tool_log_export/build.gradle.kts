@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin)
    kotlin("kapt")
}

ksp {
    arg("ModuleName", project.name)
}

android {
    namespace = "com.imcys.bilibilias.tool_log_export"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    dataBinding {
        enable = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":common"))
    implementation(libs.constraintlayout)
    ksp(libs.kcomponent.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    detektPlugins(libs.gitlab.detekt.formatting)
    detektPlugins(libs.hbmartin.detekt.rules)
    detektPlugins(libs.detekt.rules.libraries)
    detektPlugins(libs.detekt.rules.ruleauthors)
    detektPlugins(libs.detekt.rules.compose)
    detektPlugins(libs.detekt)
    detektPlugins(libs.kure.potlin)
    detektPlugins(libs.detekt.verify.implementation)
    detektPlugins(libs.rules.detekt)
}
