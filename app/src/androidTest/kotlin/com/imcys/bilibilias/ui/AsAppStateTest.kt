package com.imcys.bilibilias.ui

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.junit4.createComposeRule
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

    // Create the test dependencies.
    private val networkMonitor = TestNetworkMonitor()

    // Subject under test.
    private lateinit var state: AsAppState

    @Test
    fun niaAppState_currentDestination() = runTest {
        var currentDestination: String? = null

        composeTestRule.setContent {
//            val navController = rememberTestNavController()
//            state = remember(navController) {
//                AsAppState(
//                    navController = navController,
//                    coroutineScope = backgroundScope,
//                    networkMonitor = networkMonitor,
//                    userNewsResourceRepository = userNewsResourceRepository,
//                    timeZoneMonitor = timeZoneMonitor,
//                )
//            }

            // Update currentDestination whenever it changes
//            currentDestination = state.currentDestination?.route

            // Navigate to destination b once
            LaunchedEffect(Unit) {
//                navController.setCurrentDestination("b")
            }
        }

        assertEquals("b", currentDestination)
    }

    @Test
    fun niaAppState_destinations() = runTest {
        composeTestRule.setContent {
//            state = rememberNiaAppState(
//                networkMonitor = networkMonitor,
//                userNewsResourceRepository = userNewsResourceRepository,
//                timeZoneMonitor = timeZoneMonitor,
//            )
        }

//        assertEquals(3, state.topLevelDestinations.size)
//        assertTrue(state.topLevelDestinations[0].name.contains("for_you", true))
//        assertTrue(state.topLevelDestinations[1].name.contains("bookmarks", true))
//        assertTrue(state.topLevelDestinations[2].name.contains("interests", true))
    }

    @Test
    fun niaAppState_whenNetworkMonitorIsOffline_StateIsOffline() =
        runTest(UnconfinedTestDispatcher()) {
            composeTestRule.setContent {
//            state = NiaAppState(
//                navController = NavHostController(LocalContext.current),
//                coroutineScope = backgroundScope,
//                networkMonitor = networkMonitor,
//                userNewsResourceRepository = userNewsResourceRepository,
//                timeZoneMonitor = timeZoneMonitor,
//            )
            }

            backgroundScope.launch { state.isOffline.collect() }
            networkMonitor.setConnected(false)
//            assertEquals(
//                true,
//                state.isOffline.value,
//            )
        }

    @Test
    fun niaAppState_differentTZ_withTimeZoneMonitorChange() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
//            state = NiaAppState(
//                navController = NavHostController(LocalContext.current),
//                coroutineScope = backgroundScope,
//                networkMonitor = networkMonitor,
//                userNewsResourceRepository = userNewsResourceRepository,
//                timeZoneMonitor = timeZoneMonitor,
//            )
        }
//        val changedTz = TimeZone.of("Europe/Prague")
//        backgroundScope.launch { state.currentTimeZone.collect() }
//        timeZoneMonitor.setTimeZone(changedTz)
//        assertEquals(
//            changedTz,
//            state.currentTimeZone.value,
//        )
    }
}

//@Composable
//private fun rememberTestNavController(): TestNavHostController {
//    val context = LocalContext.current
//    return remember {
//        TestNavHostController(context).apply {
//            navigatorProvider.addNavigator(ComposeNavigator())
//            graph = createGraph(startDestination = "a") {
//                composable("a") { }
//                composable("b") { }
//                composable("c") { }
//            }
//        }
//    }
//}
