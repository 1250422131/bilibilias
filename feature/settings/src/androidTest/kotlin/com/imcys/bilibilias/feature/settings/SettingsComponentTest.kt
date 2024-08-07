package com.imcys.bilibilias.feature.settings

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

@HiltAndroidTest
class SettingsComponentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BeforeTest
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test() = runTest {
    }
}
