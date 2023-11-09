package com.imcys.player.sheet

import android.content.ClipData
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.model.Dash
import com.imcys.player.QUALITY_1080P
import com.imcys.player.QUALITY_1080P_60
import com.imcys.player.QUALITY_1080P_PLUS
import com.imcys.player.QUALITY_240P
import com.imcys.player.QUALITY_360P
import com.imcys.player.QUALITY_480P
import com.imcys.player.QUALITY_4K
import com.imcys.player.QUALITY_720P
import com.imcys.player.QUALITY_720P_60
import com.imcys.player.QUALITY_8K
import com.imcys.player.QUALITY_DOLBY
import com.imcys.player.QUALITY_HDR
import com.imcys.player.getMimeType
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SheetDirectLink(video: ImmutableList<Dash.Video>, audio: ImmutableList<Dash.Audio>, dolby: Dash.Dolby?) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        video.groupBy { it.id }.forEach { (k, v) ->
            stickyHeader {
                Text(
                    text = qualityMapToText(k),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
            }
            items(v.sortedBy { it.codecid }, key = { it.baseUrl }) {
                var expanded by remember { mutableStateOf(false) }
                Card(onClick = { expanded = !expanded }) {
                    Column(Modifier.padding(8.dp)) {
                        Row {
                            Text(
                                text = "解码器: " + getMimeType(it.codecs),
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
                                    .padding(4.dp),
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (expanded) {
                                    Icons.Default.KeyboardArrowUp
                                } else {
                                    Icons.Default.KeyboardArrowDown
                                },
                                contentDescription = null,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        DirectLinkText(
                            it.baseUrl,
                            Modifier
                                .padding(vertical = 4.dp)
                        )
                        if (expanded) {
                            it.backupUrl.forEach { back ->
                                DirectLinkText(
                                    back,
                                    Modifier
                                        .animateItemPlacement(tween(300,))
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DirectLinkText(text: String, modifier: Modifier) {
    val context = LocalContext.current
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .clickable {
                textCopyThenPost(text, context)
            }
            .padding(8.dp)
    )
}

fun textCopyThenPost(textCopied: String, context: Context) {
    val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("", textCopied))
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
    }
}

private fun qualityMapToText(k: Int) = when (k) {
    QUALITY_8K -> "8K 超高清"
    QUALITY_DOLBY -> "杜比视界"
    QUALITY_HDR -> "HDR 真彩色"
    QUALITY_4K -> "4K 超清"
    QUALITY_1080P_60 -> "1080P60 高帧率"
    QUALITY_1080P_PLUS -> "1080P+ 高码率"
    QUALITY_1080P -> "1080P 高清"
    QUALITY_720P_60 -> "720P60 高帧率"
    QUALITY_720P -> "720P 高清"
    QUALITY_480P -> "480P 清晰"
    QUALITY_360P -> "360P 流畅"
    QUALITY_240P -> "240P 极速"
    else -> "未知"
}
