package com.imcys.bilibilias

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.metrics.performance.JankStats
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.hjq.toast.Toaster
import com.imcys.bilibilias.core.analytics.AnalyticsHelper
import com.imcys.bilibilias.core.analytics.LocalAnalyticsHelper
import com.imcys.bilibilias.core.data.toast.ToastMachine
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.designsystem.theme.AsTheme
import com.imcys.bilibilias.splash.RootContent
import com.imcys.bilibilias.startup.StartupComponent
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
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var toastMachine: ToastMachine

    @Inject
    lateinit var startupComponentFactory: StartupComponent.Factory

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
            val rootComponent = startupComponentFactory(componentContext)
            AsTheme {
                CompositionLocalProvider(
                    LocalAnalyticsHelper provides analyticsHelper,
                ) {
                    val appState = rememberNiaAppState(
                        toastMachine = toastMachine,
                        networkMonitor = networkMonitor,
                        windowSizeClass = calculateWindowSizeClass(this)
                    )
                    RootContent(rootComponent, appState, componentContext)
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
    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toaster.show(R.string.app_HomeActivity_exit)
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
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
