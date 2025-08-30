package com.imcys.bilibilias

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.imcys.bilibilias.core.context.BuildConfig
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.context.LocalKmpContext
import com.imcys.bilibilias.logic.root.DefaultAppComponentContext
import com.imcys.bilibilias.logic.root.DefaultRootComponent
import com.imcys.bilibilias.ui.root.AsApp
import com.imcys.bilibilias.ui.runtime.LocalLifecycleOwner
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        val notifier = get<Notifier>()
//        notifier.postNotifications()

        val searchText = getSearchTextFromIntent()

        val context = DefaultAppComponentContext(defaultComponentContext(), get(), get())
        val component = DefaultRootComponent(context, searchText)
        val lifecycleOwner = createLifecycleOwner()

        setContent {
            CompositionLocalProvider(
                LocalKmpContext provides KmpContext,
                LocalLifecycleOwner provides lifecycleOwner,
            ) {
                AsApp(component)
            }
        }
    }

    private fun createLifecycleOwner(): LifecycleOwner {
        return object : LifecycleOwner {
            override val lifecycle get() = essentyLifecycle()
        }
    }

    private fun getSearchTextFromIntent(): String? {
        val intentSearchText = intent.getStringExtra(Intent.EXTRA_TEXT)
        return intentSearchText ?: if (BuildConfig.debugBuild) { // Use BuildConfig.DEBUG
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
