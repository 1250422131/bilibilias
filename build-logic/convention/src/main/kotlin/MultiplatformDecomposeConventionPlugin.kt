import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class MultiplatformDecomposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            dependencies {
                "implementation"(libs.findLibrary("decompose").get())
                "implementation"(libs.findLibrary("decompose.compose").get())
                "implementation"(libs.findLibrary("molecule").get())
            }
        }
    }
}
