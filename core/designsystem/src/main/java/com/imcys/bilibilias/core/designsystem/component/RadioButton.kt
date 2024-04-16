package com.imcys.bilibilias.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.imcys.bilibilias.core.designsystem.theme.AsTheme

@Composable
fun AsRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    enable: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enable,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            ),
        )
        text()
    }
}

@ThemePreviews
@Preview(showBackground = true)
@Composable
private fun PreviewRadioButton() {
    AsTheme {
        Row {
            AsRadioButton(true, {}, text = { Text(text = "H265") })
            AsRadioButton(true, {}, text = { Text(text = "AV1") })
            AsRadioButton(false, {}, text = { Text(text = "H264") })
        }
    }
}
