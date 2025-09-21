package com.imcys.bilibilias.ui.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
inline fun <T> ASToggleButtonRowGroup(
    items: List<T>,
    crossinline itemsContent: @Composable (item: T) -> Unit,
    crossinline rule: (item: T) -> Boolean,
    crossinline key: (item: T) -> Any,
    noinline onCheckedChange: ((item: T, checked: Boolean) -> Unit)? = null,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items, key = { key(it) }) { item ->
            ToggleButton(
                checked = rule(item),
                onCheckedChange = {
                    onCheckedChange?.invoke(item, it)
                },
            ) {
                itemsContent(item)
            }

        }
    }
}