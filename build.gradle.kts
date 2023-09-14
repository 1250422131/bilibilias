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
    toolVersion = "1.23.1"
    config.setFrom(file("$rootDir/config/detekt.yml"))
    parallel = true
    baseline = file("$rootDir/config/reports/baseline.xml")
    basePath = rootDir.absolutePath
    autoCorrect = true
    allRules = true
    disableDefaultRuleSets = true
}

val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("$rootDir/config/reports/merge.xml"))
    output.set(rootProject.layout.buildDirectory.file("$rootDir/config/reports/merge.sarif"))
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    tasks.named("detekt", io.gitlab.arturbosch.detekt.Detekt::class).configure {
        reports {
            xml.required.set(true)
            html.required.set(true)
            html.outputLocation.set(file("$rootDir/config/reports/detekt.html"))
            md.required.set(true)
            md.outputLocation.set(file("$rootDir/config/reports/detekt.md"))
            sarif.required.set(true)
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        finalizedBy(reportMerge)
    }

    reportMerge {
        input.from(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().map { it.xmlReportFile }) // or .sarifReportFile
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = "17"
}

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    this.jvmTarget = "17"
}

configurations.matching { it.name == "detekt" }.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.9.0")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
