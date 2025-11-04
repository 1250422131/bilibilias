package com.imcys.bilibilias.weight

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.R
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
                contentDescription = stringResource(R.string.home_text_2468),
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


@Composable
@Preview
fun UserWorkCard(
    modifier: Modifier = Modifier,
    bvId: String = "",
    title: String = "标题",
    pic: String = "",
    upName: String = "UP主",
    mid: Long = 0,
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
                contentDescription = stringResource(R.string.home_text_2468),
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                ) {
                    Text(
                        "UP",
                        fontSize = 8.sp,
                        modifier = Modifier
                            .padding(2.dp), style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }

                Spacer(Modifier.width(5.dp))

                Text(
                    upName,
                    fontSize = 12.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
    }
}


@Composable
@Preview
fun HistoryPlayVideoCard(
    modifier: Modifier = Modifier,
    bvId: String = "",
    title: String = "标题",
    pic: String = "",
    upName: String = "UP主",
    mid: Long = 0,
    duration: Long = 0,
    progress: Long = 0,
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

            Box(modifier = Modifier.aspectRatio(16f / 9f).fillMaxWidth()) {
                ASAsyncImage(
                    model = pic,
                    shape = CardDefaults.shape,
                    contentDescription = stringResource(R.string.home_text_2468),
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    if (progress == -1L) stringResource(R.string.home_text_1026) else
                        "%02d:%02d/%02d:%02d".format(
                            progress / 60, progress % 60,
                            duration / 60, duration % 60
                        ),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 3.dp),
                    color = MaterialTheme.colorScheme.surface
                )
            }


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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        shape = RoundedCornerShape(2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                    ) {
                        Text(
                            "UP",
                            fontSize = 8.sp,
                            modifier = Modifier
                                .padding(2.dp), style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }

                    Spacer(Modifier.width(5.dp))

                    Text(
                        upName,
                        fontSize = 12.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }
        }
    }
}
