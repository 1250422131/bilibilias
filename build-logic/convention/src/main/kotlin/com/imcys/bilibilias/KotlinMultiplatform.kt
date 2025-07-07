package com.imcys.bilibilias

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

/**
 * A plugin that applies the Kotlin Multiplatform plugin and configures it for the project.
 * https://github.com/cashapp/sqldelight/blob/master/buildLogic/multiplatform-convention/src/main/kotlin/app/cash/sqldelight/multiplatform/MultiplatformConventions.kt
 */
internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        // Enable native group by default
        // https://kotlinlang.org/docs/whatsnew1820.html#new-approach-to-source-set-hierarchy
        applyDefaultHierarchyTemplate {
            common {
                group("jvm") {
                    withJvm()
                    withAndroidTarget()
                }
                group("skiko") {
                    withJvm()
                }
            }
        }

        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
            unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.unitTest)
        }
        jvm("desktop")

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-Xannotation-target-all")
            freeCompilerArgs.add("-Xcontext-sensitive-resolution")
            freeCompilerArgs.add("-Xcontext-parameters")
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        // Fixes Cannot locate tasks that match ':core:model:testClasses' as task 'testClasses'
        // not found in project ':core:model'. Some candidates are: 'jsTestClasses', 'jvmTestClasses'.
        project.tasks.create("testClasses") {
            dependsOn("allTests")
        }
    }
}