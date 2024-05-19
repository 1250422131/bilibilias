import com.imcys.bilibilias.configureDecompose
import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class MoleculeLogicComponents : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("app.cash.molecule")
            dependencies {
                add("implementation", libs.findLibrary("decompose").get())
                add("implementation", libs.findLibrary("decompose.compose").get())
            }
        }
    }
}

