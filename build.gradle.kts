buildscript {
    repositories {
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/apache-snapshots")
        google()
        mavenCentral()
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
    alias(libs.plugins.kotlin.kapt)
}

detekt {
    config.setFrom(file("$projectDir/config/detekt.yml"))
    baseline = file("$projectDir/config/baseline.xml")
    parallel = true
}

dependencies {
    detektPlugins(libs.gitlab.detekt.formatting)
    detektPlugins(libs.hbmartin.detekt.rules)
    detektPlugins(libs.detekt.rules.libraries)
    detektPlugins(libs.detekt.rules.ruleauthors)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
