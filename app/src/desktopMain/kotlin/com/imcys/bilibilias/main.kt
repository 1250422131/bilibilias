package com.imcys.bilibilias

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.imcys.bilibilias.ui.AsApp
import com.imcys.bilibilias.ui.rememberAsAppState
import io.github.vinceglb.filekit.FileKit
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

fun main() {
    FileKit.init("BilibiliAs")
    application {
        KoinApplication(
            {
                modules(platformModule(), commonModules())
                startCommonKoinModule(koin.get())
            }
        ) {
            val windowState = rememberWindowState()

            Window(
                state = windowState,
                title = "BilibiliAs",
                onCloseRequest = ::exitApplication
            ) {
                val appState = rememberAsAppState(koinInject(), koinInject())
                CompositionLocalProvider {
                    AsApp(appState, koinInject())
                }
            }
        }
    }
}