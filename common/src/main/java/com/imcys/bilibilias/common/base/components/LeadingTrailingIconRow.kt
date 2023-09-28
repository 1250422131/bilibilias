package com.imcys.bilibilias.common.base.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LeadingTrailingIconRow(
    leadingIcon: @Composable () -> Unit,
    leadingText: @Composable () -> Unit,
    trailingText: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        leadingIcon()
        leadingText()
        Spacer(modifier = Modifier.weight(1f))
        trailingText()
        trailingIcon()
    }
}
