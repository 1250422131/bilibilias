package com.imcys.bilibilias.feature.settings

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.feature.settings.component.DefaultSettingsComponent
import com.imcys.bilibilias.feature.settings.component.SettingsComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test

@HiltAndroidTest
class SettingsComponentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var component: SettingsComponent

    @Inject
    private lateinit var asPreferencesDataSource: AsPreferencesDataSource

    @Inject
    private lateinit var loginInfoDataSource: LoginInfoDataSource

    @BeforeTest
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test() = runTest {

    }
}
