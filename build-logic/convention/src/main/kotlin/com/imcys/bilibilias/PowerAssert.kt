package com.imcys.bilibilias

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun Project.configurePowerAssert() {
    apply(plugin = "org.jetbrains.kotlin.plugin.power-assert")

    configure<PowerAssertGradleExtension> {
        functions = listOf(
            "kotlin.assert",
            "kotlin.test.assertTrue",
            "kotlin.test.assertEquals",
            "kotlin.test.assertNull",
        )
    }
}
