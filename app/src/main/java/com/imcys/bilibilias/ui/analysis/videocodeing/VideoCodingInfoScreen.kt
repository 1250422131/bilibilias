package com.imcys.bilibilias.ui.analysis.videocodeing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import kotlinx.serialization.Serializable

@Serializable
object VideoCodingInfoRoute : NavKey

private val keywordColorMap = mapOf(
    "CodecTag" to Color(0xFF1976D2),
    "编码格式" to Color(0xFF388E3C),
    "AV1" to Color(0xFFD32F2F),
    "HEVC" to Color(0xFF7B1FA2),
    "AVC" to Color(0xFF0288D1),
    "hev1" to Color(0xFF388E3C),
    "hvc1" to Color(0xFFFBC02D),
    "dvh1" to Color(0xFFEF6C00),
    "PQ" to Color(0xFF00897B),
    "HLG" to Color(0xFF6D4C41),
    "Profile8.4" to Color(0xFF5E35B1),
    "Profile5" to Color(0xFF00838F),
    "P8.4" to Color(0xFF5E35B1),
    "P5" to Color(0xFF00838F)
)

private fun highlightKeywords(
    text: String,
    keywordColorMap: Map<String, Color>,
    defaultStyle: SpanStyle = SpanStyle()
): AnnotatedString {
    val sortedKeywords = keywordColorMap.keys.sortedByDescending { it.length }
    val builder = AnnotatedString.Builder()
    var i = 0
    while (i < text.length) {
        var matched: String? = null
        for (kw in sortedKeywords) {
            if (text.regionMatches(i, kw, 0, kw.length)) {
                matched = kw
                break
            }
        }
        if (matched != null) {
            builder.withStyle(SpanStyle(color = keywordColorMap[matched]!!, fontWeight = FontWeight.Bold)) {
                append(matched)
            }
            i += matched.length
        } else {
            builder.withStyle(defaultStyle) {
                append(text[i])
            }
            i++
        }
    }
    return builder.toAnnotatedString()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun VideoCodingInfoScreen(onToBack: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    VideoCodingInfoScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = {}
    ) { paddingValues ->
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = "Tips：感谢明智小锐总结本篇内容",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(10.dp))
            }
            
            item {
                Text(
                    text = highlightKeywords(
                        "● 编码选择中的前者是我们比较常见的编码格式，后者则是各种编码方式标的CodecTag,可以理解为一种签辨识。",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        "● 视频的编码会影响视频的压缩效率:AV1>HEVC>AVC,同时效率高对设备解码能力要求也高,兼容性也更差（出现较晚 部分老设备不支持)。",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        "★ 根据B站的实际情况,如果你想获得最优画质或兼容老设备建议选择AVC编码(体积大),均衡选择HEVC编码,追求小体积且设备性能强的可以选AV1编码。",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        "● 而CodecTag则是各种编码中具体编码方式的标签,AVC和AV1不太复杂,B站只提供了一种CodecTag;HEVC情况比较复杂。",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        "◆ 根据我们的测试,B站的HEVC有三种CodecTag:hev1,hvc1和dvh1.",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        "• hev1,用于B站的SDR视频,以及部分HDR真彩视频也是用hev1编码的\n" +
                        "• hvc1,用于B站的HDR真彩,HDR Vivid以及杜比视界（Profile8.4）视频\n" +
                        "• dvh1 用于B站的杜比视界（Profile5）视频 为杜比专属tag",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        "■ 拓展阅读:",
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        "❶ 不同的CodecTag有什么区别?对编码有影响吗?",
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        "○ 拿hev1和hvc1举例,他们最本质的不同就是文件头(描述分辨率,帧率等信息)的写入位置不一样;hev1更加灵活,在每一个片段之前都会写一遍;hvc1在一个视频开头就定好了,所以中间不能更改;这样也就导致了文件大小由于这些文件头可能有一点点很细微的差异,不过对于画面画质来说不会有任何差异(相同参数下)",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        "❷ HDR真彩,HDR Vivid,杜比视界(Profile5&8.4)有什么不同?",
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        "○ HDR真彩就是HDR10——最基础的HDR格式,只支持静态元数据(整个视频不会有色彩亮度的动态调节) , 支持PQ和HLG两种曲线,不过B站貌似仅支持PQ曲线\n" +
                        "○ HDR Vivid是国产的HDR标准,对标杜比视界,支持PQ和HLG两种曲线,不过B站貌似仅支持PQ曲线,并且HDR Vivid视频还会输出一路HDR真彩用于web及旧版app\n" +
                        "○ 杜比视界是标准最高的HDR格式,不过其分为几个编码等级(level),杜比称作Profile,B站支持流媒体中常见的Profile8.4和Profile5\n" +
                        "▷ Profile8.4基于HLG曲线,这种曲线支持在不支持杜比视界的设备将画面调整为普通的HDR10,也可以直接映射到SDR,不会产生太大的画面失真,兼容性很好\n" +
                        "▷ Profile5基于PQ曲线,这种曲线在不支持的设备上强制播放会显得发灰,并且由于其特殊的ICtCp色彩空间,不同于常见的YCbCr,在不支持的设备上会呈现色彩映射错误的紫绿色,不过在支持的设备上Profile5显示效果是会比Profile8.4更好的",
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        "☆ 所以B站杜比视界并不是对每个设备都满血提供,P8.4大家都支持,P5可就不一定咯,所以下载到dvh1时最好打开B站看看能不能正常打开播放杜比视界,确保可以正常播放再下载.",
                        keywordColorMap
                    ),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = "（以上内容如有错漏 劳烦各位大佬指正）",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoCodingInfoScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = {
                    Text(text = "视频编码解释")
                },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                }
            )
        },
    ) {
        content(it)
    }
}