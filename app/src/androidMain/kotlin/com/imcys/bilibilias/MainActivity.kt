package com.imcys.bilibilias

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.navigation.entryProviderBuilders
import com.imcys.bilibilias.ui.AsApp
import com.imcys.bilibilias.ui.rememberAsAppState
import com.imcys.bilibilias.ui.theme.AsTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : ComponentActivity(), KoinComponent {

    private val errorMonitor: ErrorMonitor = get()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        val notifier = get<Notifier>()
//        notifier.postNotifications()

        getSearchTextFromIntent()

        setContent {
//            val backStackViewModel: AsBackStackViewModel = koinViewModel()
            val appState = rememberAsAppState(
                errorMonitor,
                asBackStack = get(),
            )
            CompositionLocalProvider {
                AsTheme(false) {
                    AsApp(appState, entryProviderBuilders)
                }
            }
        }
    }

    private fun getSearchTextFromIntent(): String? {
        val intentSearchText = intent.getStringExtra(Intent.EXTRA_TEXT)
        return intentSearchText ?: if (BuildConfig.DEBUG) { // Use BuildConfig.DEBUG
            generateRandomTestSearchText()
        } else {
            null
        }
    }

    private fun generateRandomTestSearchText(): String {
        return getSampleSearchQueries().random()
    }

    private fun getSampleSearchQueries() = listOf("BV1qW4y1k7yh")
}
