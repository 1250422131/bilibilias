buildscript {
    dependencies {
        classpath(libs.wireGradlePlugin)
    }
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        mavenCentral()
        google()
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
    alias(libs.plugins.room) apply false
    alias(libs.plugins.detekt)
}
detekt {
    toolVersion = "1.23.4"
    config.setFrom(file("$rootDir/config/detekt.yml"))
    baseline = file("$rootDir/config/reports/baseline.xml")
    parallel = true
    basePath = rootDir.absolutePath
    autoCorrect = true
}
val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("$rootDir/config/reports/merge.sarif"))
}
dependencies {
    detektPlugins(libs.twitter.detekt)
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")
}
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
    input.from(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().map { it.sarifReportFile })
}

subprojects {
    // ./gradlew assembleRelease -PcomposeCompilerReports=true
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs += arrayOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                            project.layout.buildDirectory + "/compose_compiler"
                )
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs += arrayOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                            project.layout.buildDirectory + "/compose_compiler"
                )
            }
        }
    }
}
