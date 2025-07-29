package com.imcys.bilibilias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.context.LocalKmpContext
import com.imcys.bilibilias.logic.root.RootComponent
import com.imcys.bilibilias.ui.root.AsApp
import com.imcys.bilibilias.ui.runtime.LocalLifecycleOwner
import org.koin.android.scope.AndroidScopeComponent
import org.koin.core.scope.Scope

class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val root by activityScope.inject<RootComponent>()
        val lifecycleOwner = object : LifecycleOwner {
            override val lifecycle get() = essentyLifecycle()
        }

        setContent {
            CompositionLocalProvider(
                LocalKmpContext provides KmpContext,
                LocalLifecycleOwner provides lifecycleOwner,
            ) {
                AsApp(root)
            }
        }
    }

    override val scope: Scope = activityScope
}
