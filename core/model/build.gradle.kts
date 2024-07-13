plugins {
    alias(libs.plugins.bilibilias.jvm.library)
    alias(libs.plugins.kotlin.serialization)
    `java-test-fixtures`
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
