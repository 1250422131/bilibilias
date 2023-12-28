package com.imcys.bilibilias.back

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import com.imcys.common.utils.LifecycleEventEffect

@Composable
internal fun BackHandlerWithLifecycle(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    var refreshBackHandler by rememberSaveable { mutableStateOf(false) }
    LifecycleEventEffect { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP  -> refreshBackHandler = false
            Lifecycle.Event.ON_START -> refreshBackHandler = true
            else                     -> Unit
        }
    }

    if (refreshBackHandler) {
        BackHandler(enabled = enabled) {
            onBack()
        }
    }
}