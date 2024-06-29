import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.imcys.bilibilias.applyPlugin
import com.imcys.bilibilias.configureAndroidCompose
import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.the

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension: CommonExtension<*, *, *, *, *, *> = when {
                pluginManager.hasPlugin("com.android.application") -> the<ApplicationExtension>()
                pluginManager.hasPlugin("com.android.library") -> the<LibraryExtension>()
                else -> TODO("Need to apply bilibilias.android.application or bilibilias.android.library firstly.")
            }
            configureAndroidCompose(extension)
        }
    }
}
