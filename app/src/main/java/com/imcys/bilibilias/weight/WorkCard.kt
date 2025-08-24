package com.imcys.bilibilias.weight

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.utils.NumberUtils
import com.imcys.bilibilias.ui.weight.ASAsyncImage


@Composable
@Preview
fun WorkCard(
    modifier: Modifier = Modifier,
    bvId: String = "",
    title: String = "标题",
    pic: String = "",
    view: Long = 1000,
    danmu: Long = 1000
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = CardDefaults.shape,
        modifier = modifier.fillMaxWidth(),
        onClick = {
            sendAnalysisEvent(AnalysisEvent(bvId))
        }
    ) {
        Column(Modifier.padding(8.dp)) {
            ASAsyncImage(
                model = pic,
                shape = CardDefaults.shape,
                contentDescription = "视频封面",
                modifier = Modifier.aspectRatio(16f / 9f)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Row {
                Text("${NumberUtils.formatLargeNumber(view)}播放", fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text("${NumberUtils.formatLargeNumber(danmu)}弹幕", fontSize = 12.sp)
            }
        }
    }
}
