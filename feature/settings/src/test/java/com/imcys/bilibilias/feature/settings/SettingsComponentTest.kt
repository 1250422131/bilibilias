package com.imcys.bilibilias.feature.settings

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.imcys.bilibilias.core.testing.util.MainDispatcherRule
import com.imcys.bilibilias.feature.settings.component.DefaultSettingsComponent
import com.imcys.bilibilias.feature.settings.component.SettingsComponent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.todo

class SettingsComponentTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var component: SettingsComponent

    @Before
    fun setup() {
        component =
            DefaultSettingsComponent(
                componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry()),
                TODO(),
                TODO(),
            )
    }

    @Test
    fun test() = runTest {

    }

    private fun createComponent(

    ) {

    }
}
