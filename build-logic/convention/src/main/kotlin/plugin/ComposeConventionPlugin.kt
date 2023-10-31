package plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            applyAndroid<CommonExtension<*, *, *, *, *>> {
                configureCompose(libs)
            }
            applyDependencies(libs)
        }
    }

    private fun CommonExtension<*, *, *, *, *>.configureCompose(libs: VersionCatalog) {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
        }
    }

    private fun Project.applyDependencies(libs: VersionCatalog) {
        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }

}