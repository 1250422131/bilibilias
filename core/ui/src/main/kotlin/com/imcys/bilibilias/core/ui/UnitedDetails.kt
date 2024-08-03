package com.imcys.bilibilias.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.imcys.bilibilias.core.model.video.Bvid

@Composable
fun UnitedDetails(unit: UnitedDetails, onClick: (String) -> Unit) {
    var checked by remember { mutableStateOf(false) }
    ListItem(
        leadingContent = {
            AsyncImage(
                unit.cover,
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp, 72.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
            )
        },
        headlineContent = {
            Text(text = unit.title)
        },
        trailingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = !checked
                    onClick(unit.bvid)
                },
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

data class UnitedDetails(val cover: String, val title: String, val bvid: Bvid)
