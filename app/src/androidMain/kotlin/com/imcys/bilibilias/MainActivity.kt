package com.imcys.bilibilias

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation3.runtime.EntryProviderBuilder
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.navigation.AsBackStackViewModel
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.ui.AsApp
import com.imcys.bilibilias.ui.rememberAsAppState
import com.imcys.bilibilias.ui.theme.AsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : ComponentActivity(), KoinComponent {

    private val errorMonitor: ErrorMonitor = get()
    private val backStackViewModel: AsBackStackViewModel by viewModel()
    private val entryProviderBuilders: EntryProviderBuilder<AsNavKey>.() -> Unit = get()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        val notifier = get<Notifier>()
//        notifier.postNotifications()

        getSearchTextFromIntent()

        setContent {
            val appState = rememberAsAppState(
                errorMonitor,
                asBackStack = backStackViewModel.asBackStack,
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
