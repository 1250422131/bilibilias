plugins {
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.bilibilias.android.test)
}

android {
    namespace = "com.imcys.bilibilias.benchmarks"

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_BUILD_TYPE_SUFFIX", "\"\"")
    }

    buildFeatures {
        buildConfig = true
    }

    testOptions.managedDevices.devices {
        create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6Api33") {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "aosp"
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on.
    managedDevices += "pixel6Api33"

    // Don't use a connected device but rely on a GMD for consistency between local and CI builds.
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.uiautomator)
}
