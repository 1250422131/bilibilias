package com.imcys.bilibilias.common.base.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExpandableText(
    titleText: String,
    contentText: String,
    titleTextStyle: TextStyle = TextStyle(),
    minimizedMaxLines: Int = 3,
    modifier: Modifier = Modifier,
) {
    val readOnlyTitleText = remember { titleText }

    // 标题的TextMeasure
    val titleTextMeasure = rememberTextMeasurer()
    // 存储正文的宽度和高度大小值
    var contentMeasureSize by rememberSaveable(stateSaver = SizeValueSaver) {
        mutableStateOf(Size.Zero)
    }

    // 展开，收起，执行的动画 Animatable
    val animaTable = remember { Animatable(0F, Float.VectorConverter) }
    // 当前是展开还是收起
    var expanded by rememberSaveable { mutableStateOf(false) }
    // 是不是首次初始化渲染
    var isInitRender by rememberSaveable { mutableStateOf(true) }
    // 默认收起状态下，最下高度
    var collapseMinHeight by rememberSaveable { mutableFloatStateOf(0F) }
    // 展开状态下，最大高度
    var expandMaxHeight by rememberSaveable { mutableFloatStateOf(0F) }

    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(null) }
    LaunchedEffect(Unit) {
        val textLayoutResult = titleTextMeasure.measure(
            text = readOnlyTitleText,
            style = titleTextStyle,
            constraints = Constraints(
                maxWidth = contentMeasureSize.width.roundToInt(),
                maxHeight = contentMeasureSize.height.roundToInt()
            )
        )
        textLayoutResultState = textLayoutResult
    }

    val text = buildAnnotatedString { append("我们不会期待米粉的期待") }
    val textMeasure = rememberTextMeasurer()
    val textLayoutResult = textMeasure.measure(text = text, style = TextStyle(color = Color.Black, fontSize = 18.sp))
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawText(textLayoutResult = textLayoutResult)
        }
    }

}

private val SizeValueSaver = listSaver(
    save = { listOf(it.width, it.height) },
    restore = { Size(it[0], it[1]) }
)

@Preview(showBackground = true)
@Composable
fun PreviewExpandableText() {
    ExpandableText(
        "请输入你的超长文本，来吧展示，省略更多内容...请输入你的超长文本，来吧展示，省略更多内容...请输入你的超长文本，来吧展示，省略更多内容...",
        "",
        TextStyle(color = Color.Black, fontSize = 14.sp),
    )
}
