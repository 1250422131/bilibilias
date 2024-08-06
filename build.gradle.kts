buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(libs.wire.gradlePlugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.power.assert) apply false
    alias(libs.plugins.module.graph) apply true
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.compose.compiler.report.generator) apply false
}
