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
    source.setFrom("src/main/java")
    parallel = true
    baseline = file("$rootDir/config/baseline.xml")

    // Applies the config files on top of detekt's default config file. `false` by default.
    buildUponDefaultConfig = false

    // Turns on all the rules. `false` by default.
    allRules = false

    // Disables all default detekt rulesets and will only run detekt with custom rules
    // defined in plugins passed in with `detektPlugins` configuration. `false` by default.
    disableDefaultRuleSets = false

    // Adds debug output during task execution. `false` by default.
    debug = false

    // If set to `true` the build does not fail when the
    // maxIssues count was reached. Defaults to `false`.
    ignoreFailures = false

    // Android: Don't create tasks for the specified build types (e.g. "release")
    ignoredBuildTypes = listOf("release")

    // Android: Don't create tasks for the specified build flavor (e.g. "production")
    ignoredFlavors = listOf("production")

    // Android: Don't create tasks for the specified build variants (e.g. "productionRelease")
    ignoredVariants = listOf("productionRelease")

    basePath = rootDir.absolutePath

    autoCorrect = true
}

configurations.detekt {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.9.0")
        }
    }
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config.setFrom(file("$rootDir/config/detekt.yml"))
        source.setFrom("src/main/java")
        parallel = true
        baseline = file("$rootDir/config/baseline.xml")
        basePath = rootDir.absolutePath
    }
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
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        this.jvmTarget = "17"
    }
    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        this.jvmTarget = "17"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
