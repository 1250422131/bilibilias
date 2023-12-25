package com.bilias.feature.download

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Video(
    data: Any,
    checked: Boolean,
    editable: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    var selected by remember { mutableStateOf(false) }
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable {
                selected = !selected
                onCheckedChange(selected)
            },
        headlineContent = {
            Text("title")
        },
        leadingContent = {
            AsyncImage(
                model = data,
                contentDescription = null,
                modifier = Modifier.size(128.dp, 72.dp),
                contentScale = ContentScale.Crop,
            )
        },
        trailingContent = {
            if (editable) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChange)
            }
        }
    )
}

@Preview
@Composable
fun PreviewViewBox() {
    Video(
        data = "https://www.instaily.com/images/android.jpg",
        checked = false,
        editable = true,
    ) {
    }
}
