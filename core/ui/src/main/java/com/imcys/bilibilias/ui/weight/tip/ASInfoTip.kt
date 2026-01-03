package com.imcys.bilibilias.ui.weight.tip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ASInfoTip(
    modifier: Modifier = Modifier,
    enabledPadding: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = CardDefaults.shape,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .run {
                    if (enabledPadding) padding(vertical = 12.dp, horizontal = 16.dp)
                    else this
                }
        ) {
            content()
        }
    }
}
