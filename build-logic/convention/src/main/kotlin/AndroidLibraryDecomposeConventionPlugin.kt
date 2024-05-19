import com.android.build.gradle.LibraryExtension
import com.imcys.bilibilias.configureDecompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryDecomposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("app.cash.molecule")
            val extension = extensions.getByType<LibraryExtension>()
            configureDecompose(extension)
        }
    }
}