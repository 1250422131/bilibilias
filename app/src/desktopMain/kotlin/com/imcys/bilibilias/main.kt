package com.imcys.bilibilias

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.logic.root.DefaultAppComponentContext
import com.imcys.bilibilias.logic.root.DefaultRootComponent
import com.imcys.bilibilias.ui.AsApp
import com.imcys.bilibilias.ui.rememberAsAppState
import com.imcys.bilibilias.ui.runtime.LocalLifecycleOwner
import io.github.vinceglb.filekit.FileKit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import javax.swing.SwingUtilities

fun main() {
    FileKit.init("BilibiliAs")
    val lifecycle = LifecycleRegistry()
    CoroutineScope(
        CoroutineExceptionHandler { coroutineContext, throwable ->
            logger("ApplicationScope").warn(throwable) {
                "Uncaught exception in coroutine $coroutineContext"
            }
        } + SupervisorJob() + Dispatchers.IO,
    )
    application {
        KoinApplication(
            {
                modules(platformModule(), commonModules())
                startCommonKoinModule(koin.get())
            }
        ) {
            val scope = koinInject<CoroutineScope>()
            val root = runOnUiThread {
                DefaultRootComponent(
                    DefaultAppComponentContext(
                        DefaultComponentContext(lifecycle),
                        scope
                    ),
                    null,
                )
            }

            val windowState = rememberWindowState()

            Window(
                state = windowState,
                title = "BilibiliAs",
                onCloseRequest = ::exitApplication
            ) {
                LifecycleController(
                    lifecycleRegistry = lifecycle,
                    windowState = windowState,
                    windowInfo = LocalWindowInfo.current,
                )
                val errorMonitor = koinInject<ErrorMonitor>()
                val appState = rememberAsAppState(errorMonitor)
                CompositionLocalProvider(
                    LocalLifecycleOwner provides createLifecycleOwner(lifecycle),
                ) {
                    AsApp(root, appState)
                }
            }
        }
    }
}

private fun createLifecycleOwner(lifecycle: Lifecycle): LifecycleOwner {
    return object : LifecycleOwner {
        override val lifecycle get() = lifecycle
    }
}

@Composable
fun LifecycleController(
    lifecycleRegistry: LifecycleRegistry,
    windowState: WindowState,
    windowInfo: WindowInfo? = null,
) {
    LaunchedEffect(lifecycleRegistry, windowState, windowInfo) {
        combine(
            snapshotFlow(windowState::isMinimized),
            snapshotFlow { windowInfo?.isWindowFocused ?: true },
            ::Pair,
        ).collect { (isMinimized, isFocused) ->
            when {
                isMinimized -> lifecycleRegistry.stop()
                isFocused -> lifecycleRegistry.resume()
                lifecycleRegistry.state == Lifecycle.State.RESUMED -> lifecycleRegistry.pause()
                else -> lifecycleRegistry.start()
            }
        }
    }

    DisposableEffect(lifecycleRegistry) {
        lifecycleRegistry.create()
        onDispose(lifecycleRegistry::destroy)
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}