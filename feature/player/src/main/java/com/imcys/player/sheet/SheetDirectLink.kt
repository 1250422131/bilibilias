package com.imcys.player.sheet

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.os.*
import android.widget.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.imcys.common.utils.*
import com.imcys.player.*
import com.imcys.player.state.*
import kotlinx.collections.immutable.*

@Composable
internal fun SheetDirectLink(
    video: ImmutableMap<Quality, ImmutableList<PlayLinkInfo>>,
    audio: ImmutableMap<Quality, ImmutableList<PlayLinkInfo>>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        video.entries.forEach { (k, v) ->
            items(v) { item ->
                Card {
                    Column(Modifier.padding(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TextBackground(qualityMapToText(item.id))
                            TextBackground(
                                MediaUtils.getMimeType(item.codecs)?.replace("video/", "") ?: ""
                            )
                            TextBackground("${item.width}×${item.height}")
                        }
                        DirectLinkText(
                            item.baseUrl,
                            Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextBackground(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
            .padding(4.dp),
        fontSize = 11.sp
    )
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
    val clipboardManager =
        context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
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
