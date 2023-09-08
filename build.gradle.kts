buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/apache-snapshots")
        maven("https://jitpack.io")
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
    alias(libs.plugins.kotlin.kapt) apply false
}

detekt {
    config.setFrom(file("$projectDir/config/detekt.yml"))
    baseline = file("$projectDir/config/baseline.xml")
    parallel = true
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config.setFrom(file("$projectDir/config/detekt.yml"))
        tasks.named("detekt", io.gitlab.arturbosch.detekt.Detekt::class).configure {
            reports {
                // Enable/Disable XML report (default: true)
                xml.required.set(true)
                xml.outputLocation.set(file("$projectDir/config/detekt.xml"))
                // Enable/Disable HTML report (default: true)
                html.required.set(true)
                html.outputLocation.set(file("$projectDir/config/detekt.html"))
                // Enable/Disable MD report (default: false)
                md.required.set(true)
                md.outputLocation.set(file("$projectDir/config/detekt.md"))
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
