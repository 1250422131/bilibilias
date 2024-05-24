import app.cash.molecule.gradle.MoleculeExtension
import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class MultiplatformDecomposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "app.cash.molecule")
            extensions.configure<MoleculeExtension> {
                kotlinCompilerPlugin = "org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.0.0"
            }
            dependencies {
                "implementation"(libs.findLibrary("decompose").get())
                "implementation"(libs.findLibrary("decompose.compose").get())
            }
        }
    }
}
