import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class MultiplatformDecomposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(libs.findLibrary("decompose").get())
                "implementation"(libs.findLibrary("decompose.compose").get())
            }
        }
    }
}
