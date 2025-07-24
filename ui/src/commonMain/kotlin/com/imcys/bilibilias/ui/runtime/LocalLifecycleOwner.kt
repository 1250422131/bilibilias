package com.imcys.bilibilias.ui.runtime

import androidx.compose.runtime.compositionLocalOf
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

val LocalLifecycleOwner = compositionLocalOf<LifecycleOwner> {
    object : LifecycleOwner {
        override val lifecycle: Lifecycle = LifecycleRegistry()
    }
}