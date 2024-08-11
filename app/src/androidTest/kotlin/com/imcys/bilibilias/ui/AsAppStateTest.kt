package com.imcys.bilibilias.ui

import androidx.compose.ui.test.junit4.createComposeRule
import com.imcys.bilibilias.core.testing.util.TestErrorMonitor
import com.imcys.bilibilias.core.testing.util.TestNetworkMonitor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class AsAppStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val networkMonitor = TestNetworkMonitor()

    private val errorMonitor = TestErrorMonitor(networkMonitor)

    private lateinit var state: AsAppState

    @Test
    fun asAppState_whenNetworkMonitorIsOnline_StateIsOnline() =
        runTest(UnconfinedTestDispatcher()) {
            composeTestRule.setContent {
                state = AsAppState(
                    coroutineScope = backgroundScope,
                    errorMonitor = errorMonitor,
                )
            }

            backgroundScope.launch { state.isOfflineState.collect() }
            networkMonitor.setConnected(true)
            assertEquals(
                false,
                state.isOfflineState.value,
            )
        }
}
