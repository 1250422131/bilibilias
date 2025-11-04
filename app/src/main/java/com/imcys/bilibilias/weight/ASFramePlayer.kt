package com.imcys.bilibilias.weight

import androidx.compose.foundation.Image
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Bitmap
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ASFramePlayer(modifier: Modifier, list: List<Bitmap>, fps: Int) {
    if (list.isEmpty()) return
    val totalFrames = list.size
    val duration = ceil(totalFrames / fps * 1f).toInt()
    var currentFrame by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var isDragging by remember { mutableStateOf(false) }

    // 自动播放逻辑
    LaunchedEffect(list, isPlaying, isDragging) {
        while (isPlaying && !isDragging) {
            delay(1000L / fps)
            currentFrame = (currentFrame + 1) % totalFrames
        }
    }

    val valueRange = 0f..(totalFrames - 1).toFloat()

    Box(modifier = modifier) {
        Image(
            bitmap = list[currentFrame].asImageBitmap(),
            contentDescription = stringResource(R.string.home_text_5371),
            modifier = Modifier.fillMaxSize()
        )
        Text(
            "${currentFrame + 1} / $totalFrames",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 10.dp, top = 10.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
        ) {
            // 进度条
            Slider(
                value = currentFrame.toFloat(),
                onValueChange = {
                    isDragging = true
                    currentFrame = it.toInt().coerceIn(0, totalFrames - 1)
                },
                onValueChangeFinished = {
                    isDragging = false
                },
                valueRange = valueRange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp),
                track = { positions ->
                    val fraction = (positions.value - valueRange.start) /
                            (valueRange.endInclusive - valueRange.start)
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                },
                thumb = { },
            )
            // 进度显示
            Text(
                text = "${((currentFrame + 1) / fps.toFloat()).format1()}s / ${duration}s",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// 保留一位小数的扩展函数
private fun Float.format1(): String = String.format(Locale.US, "%.1f", this)
