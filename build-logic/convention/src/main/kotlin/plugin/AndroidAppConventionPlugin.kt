package plugin

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class AndroidAppConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.applyPlugin()
            extensions.configure<ApplicationExtension> {
                configureAndroid()
                configureKotlin(this)
            }
            applyDependencies()
        }
    }

    private fun PluginManager.applyPlugin() {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("kotlin-parcelize")
        apply("bilibiliAs.android.lint")
    }

    private fun ApplicationExtension.configureAndroid() {
        configureAndroidCommon()

        defaultConfig.targetSdk = 34

        buildTypes {
            release {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            implementationDefaultTestDependencies(libs)
        }
    }
}
