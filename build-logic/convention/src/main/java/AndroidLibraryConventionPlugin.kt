import com.android.build.gradle.LibraryExtension
import com.imcys.bilibilias.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import com.imcys.bilibilias.buildlogic.libs

class AndroidLibraryConventionPlugin  : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                // testOptions.animationsDisabled = true
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"
            }

            dependencies{
                add("testImplementation",libs.findLibrary("junit").get())
                add("androidTestImplementation",libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation",libs.findLibrary("androidx-espresso-core").get())
//                add("androidTestImplementation",libs.findLibrary("androidx-ui-test-junit4").get())
//                add("debugImplementation",libs.findLibrary("androidx-ui-tooling").get())
//                add("debugImplementation",libs.findLibrary("androidx-test-manifest").get())
            }

        }
    }


}