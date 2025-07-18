package com.imcys.bilibilias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.context.LocalKmpContext
import com.imcys.bilibilias.logic.root.DefaultRootComponent
import com.imcys.bilibilias.ui.root.AsApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val root = DefaultRootComponent(defaultComponentContext())
        setContent {
            CompositionLocalProvider(
                LocalKmpContext provides KmpContext
            ) {
                AsApp(root)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}