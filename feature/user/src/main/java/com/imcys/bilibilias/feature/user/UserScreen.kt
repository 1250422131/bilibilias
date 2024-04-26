package com.imcys.bilibilias.feature.user

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UserRoute() {
    Scaffold { innerPading ->
        Column(modifier = Modifier.padding(innerPading)) {
            ListItem(
                modifier = Modifier.padding(horizontal = 20.dp),
                leadingContent = {
                    AsyncImage(
                        model = "http://i1.hdslb.com/bfs/face/d15f3ad5c4463aa8b42bcdab614ee3b737834944.jpg",
                        contentDescription = "头像",
                        modifier = Modifier.size(90.dp).clip(CircleShape),
                    )
                },
                headlineContent = {
                    Text(text = "name")
                },
                supportingContent = { Text(text = "sing") },
            )
            Row(modifier = Modifier.padding(8.dp),) {
                val modifier = Modifier.weight(1f)
                ImageWithText(R.drawable.ic_item_user_collection, "收藏", modifier = modifier) {}
                ImageWithText(R.drawable.ic_item_user_play_history, "历史", modifier = modifier) {}
                ImageWithText(R.drawable.ic_item_bangumi_followr, "追番", modifier = modifier) {}
            }
        }
    }
}

@Composable
internal fun UserScreen() {
}

@Composable
fun ImageWithText(
    @DrawableRes resId: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(text = label)
    }
}
