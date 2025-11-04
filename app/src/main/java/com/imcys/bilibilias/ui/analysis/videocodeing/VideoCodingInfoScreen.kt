package com.imcys.bilibilias.ui.analysis.videocodeing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.res.stringResource
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
import com.imcys.bilibilias.R
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import kotlinx.serialization.Serializable

@Serializable
object VideoCodingInfoRoute : NavKey

private val keywordColorMap = mapOf(
    "CodecTag" to Color(0xFF1976D2),
    stringResource(R.string.home_text_3162) to Color(0xFF388E3C),
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
                    text = stringResource(R.string.home_text_6476),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(10.dp))
            }
            
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_text_4138),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_support),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_text_6662),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_text_4232),
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
                        stringResource(R.string.home_text_3158) +
                        "• hvc1,用于B站的HDR真彩,HDR Vivid以及杜比视界（Profile8.4）视频\n" +
                        stringResource(R.string.home_text_1348),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_text_4021),
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_text_7621),
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.home_text_8127),
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
                        stringResource(R.string.home_support_1) +
                        stringResource(R.string.home_support_2) +
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
                    text = stringResource(R.string.home_text_7239),
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
                    Text(text = stringResource(R.string.home_text_5404))
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