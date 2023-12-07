package com.imcys.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IconArrowBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick, modifier) {
        Icon(Icons.Default.ArrowBack, contentDescription = "ArrowBack")
    }
}
