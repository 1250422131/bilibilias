import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.imcys.bilibilias.configureTestOptionsUnitTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

class AndroidTestOptionsUnitTestsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension: CommonExtension<*, *, *, *, *, *> = when {
                pluginManager.hasPlugin("com.android.application") -> the<ApplicationExtension>()
                pluginManager.hasPlugin("com.android.library") -> the<com.android.build.api.dsl.LibraryExtension>()
                else -> TODO("This plugin is dependent on either bilibilias.android.application or bilibilias.android.library. Apply one of those plugins first.")
            }
            configureTestOptionsUnitTests(extension)
        }
    }
}