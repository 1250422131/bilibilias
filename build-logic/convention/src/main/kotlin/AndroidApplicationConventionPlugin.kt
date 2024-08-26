import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.imcys.bilibilias.applyPlugin
import com.imcys.bilibilias.configureBadgingTasks
import com.imcys.bilibilias.configureGradleManagedDevices
import com.imcys.bilibilias.configureKotlinAndroid
import com.imcys.bilibilias.configurePrintApksTask
import com.imcys.bilibilias.configureResourcesPackaging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            applyPlugin("bilibilias.android.lint")
            applyPlugin("bilibilias.detekt")
            applyPlugin("dependencyGuard")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
                configureResourcesPackaging(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
                configureBadgingTasks(extensions.getByType<BaseExtension>(), this)
            }
        }
    }
}
