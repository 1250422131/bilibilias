plugins {
    `java-library`
    kotlin("jvm")
    alias(libs.plugins.bilibilias.android.lint)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.lint.api)
    testImplementation(libs.lint.checks)
    testImplementation(libs.lint.tests)
    testImplementation(kotlin("test"))
}
