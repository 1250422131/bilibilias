package com.imcys.bilibilias.ui.analysis.videocodeing


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
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
    "格式" to Color(0xFF388E3C),
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
                    text = stringResource(R.string.app_content_1),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(10.dp))
            }
            
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_yes),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_video),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_select),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_yes_2),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_hevc_codectag_types),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_yes_7) +
                        stringResource(R.string.app_video_2) +
                        stringResource(R.string.app_video_1),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            item {
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_extended_reading),
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_codec_tag_question),
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_yes_3),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_hdr_formats_question),
                        keywordColorMap
                    ),
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_yes_5) +
                        stringResource(R.string.app_yes_6) +
                        stringResource(R.string.app_yes_1) +
                        stringResource(R.string.app_profile84_hlg_description) +
                        stringResource(R.string.app_error_1),
                        keywordColorMap
                    ),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = highlightKeywords(
                        stringResource(R.string.app_download),
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
                    text = stringResource(R.string.app_content),
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
                    Text(text = stringResource(R.string.app_video_3))
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