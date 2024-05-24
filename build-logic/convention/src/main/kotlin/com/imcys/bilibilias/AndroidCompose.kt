package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = null
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }

//        project.providers.gradleProperty("enableComposeCompilerMetrics")
//            .onlyIfTrue().flatMap { rootProject.layout.buildDirectory.dir("compose-metrics") }
//            .let(metricsDestination::set)

//        project.providers.gradleProperty("enableComposeCompilerReports")
//            .onlyIfTrue().flatMap { rootProject.layout.buildDirectory.dir("compose-reports") }
//            .let(reportsDestination::set)

        stabilityConfigurationFile =
            rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
        metricsDestination = layout.buildDirectory.dir("compose-metrics")
        reportsDestination = layout.buildDirectory.dir("compose-reports")

        generateFunctionKeyMetaClasses = true
        includeSourceInformation = true
        enableNonSkippingGroupOptimization = true
        enableStrongSkippingMode = true
        includeTraceMarkers = true
    }
}
