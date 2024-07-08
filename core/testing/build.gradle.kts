plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.hilt)
    kotlin("plugin.power-assert")
}

android {
    namespace = "com.imcys.bilibilias.core.testing"
}

dependencies {
    api(projects.core.analytics)
    api(projects.core.data)
    api(projects.core.model)
    api(libs.kotlinx.coroutines.test)
    api(projects.core.common)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.datetime)
    implementation(testFixtures(projects.core.data))
    implementation(testFixtures(projects.core.datastore))

    testImplementation("io.strikt:strikt-core:0.34.0")
    val mockkVersion = "1.13.11"
    testImplementation("io.mockk:mockk:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-android:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-agent:${mockkVersion}")
    testImplementation("io.mockk:mockk-android:${mockkVersion}")
    testImplementation("io.mockk:mockk-agent:${mockkVersion}")
}
