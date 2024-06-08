package com.imcys.bilibilias.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UnitedDetails(unit: UnitedDetails) {
    ListItem(
        leadingContent = {
            AsyncImage(
                unit.cover,
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp, 72.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        },
        headlineContent = {
            Text(text = unit.title)
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

data class UnitedDetails(val cover: String, val title: String)
