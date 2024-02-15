@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.bilibiliAs.android.hilt)
}

android {
    namespace = "com.bilias.core.datastore"
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(projects.core.datastoreProto)
    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.mmkv)
    implementation(libs.fastkv)
    implementation(libs.androidx.datastore)

    androidTestImplementation(projects.core.testing)
}
