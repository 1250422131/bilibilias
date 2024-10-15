package com.imcys.bilibilias

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Create new Directory by [dir] name in the root build directory.
 */
internal fun Project.relativeToRootProject(dir: String): Provider<Directory> =
    rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
        .map { it.dir(dir) }

/**
 * Check a [propertyName]'s property value is true.
 */
internal fun Project.isPropertyValueIsTrue(propertyName: String): Boolean =
    properties[propertyName].toString().toBoolean()
