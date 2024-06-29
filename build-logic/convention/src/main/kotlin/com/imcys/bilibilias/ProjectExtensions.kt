package com.imcys.bilibilias

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.applyPlugin(alias: String) {
    apply(plugin = libs.findPlugin(alias).get().get().pluginId)
}

internal fun Project.version(key: String): String = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")
    .findVersion(key)
    .get()
    .requiredVersion

internal fun Project.versionInt(key: String) = version(key).toInt()

internal val Project.COMPOSE_VERSION get() = version("compose")
internal val Project.ANDROID_COMPILE_SDK_VERSION get() = versionInt("android.compilesdk")
