package com.imcys.bilibilias

import android.graphics.Color
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.metrics.performance.JankStats
import com.arkivanov.decompose.defaultComponentContext
import com.hjq.toast.Toaster
import com.imcys.bilibilias.core.analytics.AnalyticsHelper
import com.imcys.bilibilias.core.analytics.LocalAnalyticsHelper
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.designsystem.theme.AsTheme
import com.imcys.bilibilias.navigation.RootComponent
import com.imcys.bilibilias.ui.AsApp
import com.imcys.bilibilias.ui.rememberNiaAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var errorMonitor: ErrorMonitor

    @Inject
    lateinit var rootComponentFactory: RootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        backhandle()
        setContent {
            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(Unit) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { false },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { false },
                )
                onDispose {}
            }

            val componentContext = defaultComponentContext()
            AsTheme {
                CompositionLocalProvider(
                    LocalAnalyticsHelper provides analyticsHelper,
                ) {
                    val appState = rememberNiaAppState(
                        errorMonitor = errorMonitor,
                    )
                    AsApp(appState, rootComponentFactory(componentContext))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lazyStats.get().isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
    }

    private var exitTime = 0L
    private fun backhandle() {
        onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        Toaster.show(R.string.app_HomeActivity_exit)
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        )
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

// @Preview
// @Composable
// fun ScaffoldWithCoroutinesSnackbar() {
//    // decouple snackbar host state from scaffold state for demo purposes
//    // this state, channel and flow is for demo purposes to demonstrate business logic layer
//    val snackbarHostState = remember { SnackbarHostState() }
//    // we allow only one snackbar to be in the queue here, hence conflated
//    val channel = remember { Channel<Int>(Channel.CONFLATED) }
//    LaunchedEffect(channel) {
//        channel.receiveAsFlow().collect { index ->
//            val result =
//                snackbarHostState.showSnackbar(
//                    message = "Snackbar # $index",
//                    actionLabel = "Action on $index"
//                )
//            when (result) {
//                SnackbarResult.ActionPerformed -> {
//                    /* action has been performed */
//                }
//                SnackbarResult.Dismissed -> {
//                    /* dismissed, no action needed */
//                }
//            }
//        }
//    }
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) },
//        floatingActionButton = {
//            var clickCount by remember { mutableStateOf(0) }
//            ExtendedFloatingActionButton(
//                onClick = {
//                    // offset snackbar data to the business logic
//                    channel.trySend(++clickCount)
//                }
//            ) {
//                Text("Show snackbar")
//            }
//        },
//        content = { innerPadding ->
//            Text(
//                "Snackbar demo",
//                modifier = Modifier.padding(innerPadding).fillMaxSize().wrapContentSize()
//            )
//        }
//    )
// }
