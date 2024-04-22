buildscript {
    dependencies {
        classpath("com.squareup.wire:wire-gradle-plugin:4.9.9")
        classpath("app.cash.molecule:molecule-gradle-plugin:1.4.2")
    }
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
}
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dependencyGuard) apply false
}
