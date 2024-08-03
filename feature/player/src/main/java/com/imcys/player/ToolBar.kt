package com.imcys.player

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.common.utils.digitalConversion
import com.imcys.designsystem.component.CenterRow

@Composable
fun ToolBar(
    like: Int,
    coin: Int,
    favorite: Int,
    share: Int,
    isLike: Boolean,
    isCoins: Boolean,
    isFavoured: Boolean,
    modifier: Modifier = Modifier
) {
    CenterRow(
        modifier
            .fillMaxWidth()
    ) {
        ToolBarItem(
            imageId = R.drawable.ic_1,
            desc = "点赞",
            like = isLike,
            text = like.digitalConversion(),
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
        ToolBarItem(
            imageId = R.drawable.ic_2,
            desc = "投币",
            like = isCoins,
            text = coin.digitalConversion(),
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
        ToolBarItem(
            imageId = R.drawable.ic_3,
            desc = "收藏",
            like = isFavoured,
            text = favorite.digitalConversion(),
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
        ToolBarItem(
            imageId = R.drawable.ic_4,
            desc = "分享",
            like = false,
            text = share.digitalConversion(),
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
    }
}

@Composable
fun ToolBarItem(
    @DrawableRes imageId: Int,
    desc: String,
    text: String,
    like: Boolean,
    modifier: Modifier = Modifier,
    likeColor: Color = Color(0XFFFF6699)
) {
    VerticalChunk(
        top = {
            Image(
                painter = painterResource(imageId),
                contentDescription = desc,
                modifier = Modifier
                    .size(28.dp),
                colorFilter = if (like) ColorFilter.tint(likeColor) else null
            )
        },
        bottom = {
            Text(text = text, fontSize = 11.sp, color = Color(97, 102, 109))
        },
        modifier = modifier.height(60.dp),
    )
}
