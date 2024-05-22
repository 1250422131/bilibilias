import com.android.build.api.dsl.ApplicationExtension
import com.imcys.bilibilias.configureDecompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationDecomposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("app.cash.molecule")

            val extension = extensions.getByType<ApplicationExtension>()
            configureDecompose(extension)
        }
    }
}