plugins {
    alias(libs.plugins.bilibilias.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.ktor.serialization.kotlinx.json)
}
