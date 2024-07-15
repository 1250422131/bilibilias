package com.imcys.bilibilias.feature.settings

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_STORE_PATH
import com.imcys.bilibilias.core.datastore.InMemoryDataStore
import com.imcys.bilibilias.core.datastore.LoginInfo
import com.imcys.bilibilias.core.datastore.UserPreferences
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.testing.util.MainDispatcherRule
import com.imcys.bilibilias.feature.settings.component.DefaultSettingsComponent
import com.imcys.bilibilias.feature.settings.component.SettingsComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsComponentTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var component: SettingsComponent

    @BeforeTest
    fun setup() {
        component =
            DefaultSettingsComponent(
                componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry()),
                AsPreferencesDataSource(InMemoryDataStore(UserPreferences())),
                LoginInfoDataSource(InMemoryDataStore(LoginInfo())),
            )
    }

    @Test
    fun test() = testScope.runTest {
        assertEquals(DEFAULT_STORE_PATH, component.models.first().storagePath)
    }
}
