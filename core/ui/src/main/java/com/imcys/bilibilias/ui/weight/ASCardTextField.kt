package com.imcys.bilibilias.ui.weight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ASCardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "BV / AV / EP / SS 号...",
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    autoFocus: Boolean = true,
    requestFocusDelayMillis: Long = 300L,
    clearFocusWhenValueEmptied: Boolean = false
) {
    var firstOpen by remember { mutableStateOf(true) }
    var hasFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var tfv by remember { mutableStateOf(TextFieldValue(text = value)) }

    val haptics = LocalHapticFeedback.current

    LaunchedEffect(value) {
        if (value != tfv.text) {
            tfv = tfv.copy(
                text = value,
                selection = if (hasFocus) TextRange(value.length) else TextRange.Zero
            )
        }
    }

    LaunchedEffect(autoFocus, value,firstOpen) {
        if (autoFocus && value.isEmpty() && firstOpen) {
            if (requestFocusDelayMillis > 0) delay(requestFocusDelayMillis)
            runCatching { focusRequester.requestFocus() }
        }
        firstOpen = false
    }

    // 刚获得焦点时，把光标移到末尾（仅当有内容）
    LaunchedEffect(hasFocus) {
        if (hasFocus && tfv.text.isNotEmpty()) {
            tfv = tfv.copy(selection = TextRange(tfv.text.length))
        }
    }

    Card(
        modifier = modifier,
        elevation = elevation,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))

            Icon(
                Icons.Outlined.Search,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null
            )

            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                BasicTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            if (!enabled || readOnly) return@onFocusChanged
                            hasFocus = state.isFocused
                        },
                    value = tfv,
                    onValueChange = { newValue ->
                        tfv = newValue
                        onValueChange(newValue.text)
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
                    enabled = enabled,
                    readOnly = readOnly
                ) { innerTextField ->
                    Column {
                        Spacer(Modifier.height(12.dp))
                        Box {
                            if (tfv.text.isEmpty()) {
                                Text(
                                    hint,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerTextField()
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            if (tfv.text.isNotEmpty()) {
                Icon(
                    Icons.Outlined.Close,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                        tfv = tfv.copy(text = "", selection = TextRange.Zero)
                        onValueChange("")
                    }
                )
            }

            Spacer(Modifier.width(16.dp))
        }
    }
}