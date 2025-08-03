package com.imcys.bilibilias.ui.weight

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AsCardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {

    var hasFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier,
        elevation = elevation,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
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
                        .padding(horizontal = 16.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged({
                            if (!enabled || readOnly) return@onFocusChanged
                            hasFocus = it.isFocused
                        }),
                    value = value,
                    onValueChange = {
                        if (it.isEmpty()) {
                            focusManager.clearFocus()
                        }
                        onValueChange.invoke(it)
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
                        if (value.isEmpty() && !hasFocus) {
                            Text(
                                "BV / AV / EP 号...", color = MaterialTheme.colorScheme.onPrimary.copy(
                                    alpha = 0.5f
                                ), fontSize = 16.sp, modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            innerTextField()
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

        }
    }
}