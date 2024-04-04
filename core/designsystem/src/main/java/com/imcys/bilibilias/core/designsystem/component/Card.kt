package com.imcys.bilibilias.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AsCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(251, 114, 153)
        ),
        content = content
    )
}
