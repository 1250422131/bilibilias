buildscript {
    dependencies {
        classpath(libs.wire.gradle.plugin)
        classpath(libs.molecule.gradle.plugin)
    }
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.kotlin.serialization) apply false
//    id("org.jetbrains.kotlin.kapt") version "1.9.23" apply false
}
