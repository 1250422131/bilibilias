package com.imbys.bilibilias.feature.authorspace

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthorSpaceContent(component: AuthorSpaceComponent) {
}

@Composable
private fun AuthorSpaceScreen() {
}

@Preview
@Composable
private fun UnitedDetails() {
    ListItem(
        leadingContent = {
            Image(
                Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp, 72.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Fit
            )
        },
        headlineContent = {
            Text(text = "title")
        },
        supportingContent = { },
        modifier = Modifier.padding(8.dp),
        overlineContent = {}
    )
}
