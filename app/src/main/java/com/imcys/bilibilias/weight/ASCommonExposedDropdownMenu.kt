package com.imcys.bilibilias.weight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ASCommonExposedDropdownMenu(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    values: List<T>,
    onValue: (T) -> String,
    onDismissRequest: () -> Unit = {},
    onSelect: (T) -> Unit,
    onKey: (T) -> Any = { it as Any },
    onItemModifier: @Composable (T) -> Modifier = { Modifier }
) {
    var expanded by remember { mutableStateOf(false) }

    // 保证事件回调在重组时稳定
    val currentOnSelect = rememberUpdatedState(onSelect)
    val currentOnDismiss = rememberUpdatedState(onDismissRequest)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
            value = text,
            onValueChange = { },
            readOnly = true,
            singleLine = true,                       // 只读单行语义清晰
            label = { Text(label, fontSize = 12.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = CardDefaults.shape
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                currentOnDismiss.value()
            },
            shape = CardDefaults.shape,
        ) {
            values.forEach { item ->
                key(onKey(item)) {
                    val title = remember(item) { onValue(item) }
                    DropdownMenuItem(
                        text = { Text(title, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            expanded = false
                            currentOnSelect.value(item)
                        },
                        modifier = onItemModifier(item),
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}