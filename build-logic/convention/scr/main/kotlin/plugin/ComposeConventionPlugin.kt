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
            val compilerVersion = libs.findVersion("androidxComposeCompiler").get().toString()
            kotlinCompilerExtensionVersion = compilerVersion
        }
    }

    private fun Project.applyDependencies(libs: VersionCatalog) {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        dependencies {
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }

}