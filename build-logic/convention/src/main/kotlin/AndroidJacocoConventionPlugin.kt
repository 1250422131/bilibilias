import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.imcys.bilibilias.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.the

class AndroidJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jacoco")

            val androidExtension: CommonExtension<*, *, *, *, *, *> = when {
                pluginManager.hasPlugin("com.android.application") -> the<BaseAppModuleExtension>()
                pluginManager.hasPlugin("com.android.library") -> the<LibraryExtension>()
                else -> TODO("This plugin is dependent on either bilibilias.android.application or bilibilias.android.library. Apply one of those plugins first.")
            }

            val jacocoExtension: AndroidComponentsExtension<*, *, *> = when {
                pluginManager.hasPlugin("com.android.application") -> the<ApplicationAndroidComponentsExtension>()
                pluginManager.hasPlugin("com.android.library") -> the<LibraryAndroidComponentsExtension>()
                else -> TODO("This plugin is dependent on either bilibilias.android.application or bilibilias.android.library. Apply one of those plugins first.")
            }

            androidExtension.buildTypes.configureEach {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }

            configureJacoco(jacocoExtension)
        }
    }
}
