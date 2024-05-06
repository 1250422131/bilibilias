package com.imcys.bilibilias.core.designsystem.reveal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.designsystem.theme.AsTheme

@Composable
private fun RevealTest() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val isVisible = remember { mutableStateOf(false) }
        val revealFrom = remember { mutableStateOf(Offset(0f, 0f)) }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.error)
                .fillMaxWidth()
                .height(150.dp)
        )

        Box(
            modifier = Modifier
                .circularReveal(
                    isVisible = isVisible.value,
                    revealFrom = revealFrom.value
                )
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(300.dp)
        )

        DraggableCircle(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .size(50.dp)
                .align(Alignment.CenterEnd)
        ) {
            revealFrom.value = it
            isVisible.value = !isVisible.value
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RevealTestPreview() {
    AsTheme {
        RevealTest()
    }
}