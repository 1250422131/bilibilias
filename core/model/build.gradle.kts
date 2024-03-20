plugins {
    alias(libs.plugins.nowinandroid.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.imcys.bilibilias.core.model"
    compileSdk = 34
}

dependencies {
    implementation(libs.ktor.serialization.kotlinx.json)
}
