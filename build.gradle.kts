buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.detekt)
}

detekt {
    config.setFrom(file("$rootDir/config/detekt.yml"))
    baseline = file("$rootDir/config/baseline.xml")
    parallel = true
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config.setFrom(file("$rootDir/config/detekt.yml"))
        tasks.named("detekt", io.gitlab.arturbosch.detekt.Detekt::class).configure {
            reports {
                // Enable/Disable XML report (default: true)
                xml.required.set(true)
                xml.outputLocation.set(file("$rootDir/config/detekt.xml"))
                // Enable/Disable HTML report (default: true)
                html.required.set(true)
                html.outputLocation.set(file("$rootDir/config/detekt.html"))
                // Enable/Disable MD report (default: false)
                md.required.set(true)
                md.outputLocation.set(file("$rootDir/config/detekt.md"))
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
