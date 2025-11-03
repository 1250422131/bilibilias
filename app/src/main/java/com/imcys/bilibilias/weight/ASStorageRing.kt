package com.imcys.bilibilias.weight


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.utils.StorageInfoData
import com.imcys.bilibilias.common.utils.StorageUtil

@Composable
fun AnimatedStorageRing(
    storageInfoData: StorageInfoData,
    modifier: Modifier = Modifier,
    stroke: Dp = 25.dp,
    animDuration: Int = 800,
    totalColor: Color = MaterialTheme.colorScheme.surface,
    usedColor: Color = MaterialTheme.colorScheme.primaryContainer,
    appColor: Color = MaterialTheme.colorScheme.primary
) {
    val total = storageInfoData.totalBytes * 1f

    val usedRatio = (storageInfoData.usedBytes / total).coerceIn(0f, 1f)
    val appRatio =
        ((storageInfoData.downloadBytes + storageInfoData.appBytes) / total).coerceIn(0f, 1f)

    // 动画值：0 -> 目标角度
    val usedAngle = remember { Animatable(0f) }
    val appAngle = remember { Animatable(0f) }

    LaunchedEffect(usedRatio, appRatio) {
        // 两段一起动，也可以分开
        usedAngle.animateTo(
            targetValue = 360f * usedRatio,
            animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
        )
        appAngle.animateTo(
            targetValue = 360f * appRatio,
            animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
        )
    }


    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Canvas(modifier.fillMaxSize()) {
                val diameter = size.width
                val width = stroke.toPx()
                val topLeft = Offset(width / 2, width / 2)
                val arcSize = Size(diameter - width, diameter - width)

                // 1. 背景圆环（剩余空间）
                drawArc(
                    color = surfaceColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width)
                )

                // 2. 已用圆环（带动画）
                drawArc(
                    color = primaryContainerColor,
                    startAngle = -90f,
                    sweepAngle = usedAngle.value,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width, cap = StrokeCap.Round)
                )

                // 3. App 占用圆环（带动画）
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = appAngle.value,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width, cap = StrokeCap.Round)
                )
            }

            Column(
                Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${StorageUtil.formatSize(storageInfoData.appBytes + storageInfoData.downloadBytes)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // APP占用设备内存百分比
                Text(
                    stringResource(R.string.storage_zhan_yong_she_bei_nei_cun)%.2f".format(((storageInfoData.appBytes + storageInfoData.downloadBytes) / total) * 100)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(15.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically

            ) {
                Surface(shape = CircleShape, color = totalColor, modifier = Modifier.size(12.dp)) {

                }
                Text(stringResource(R.string.storage_ke_yong_kong_jian), Modifier.padding(start = 5.dp), fontSize = 14.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically

            ) {
                Surface(shape = CircleShape, color = usedColor, modifier = Modifier.size(12.dp)) {

                }
                Text(stringResource(R.string.storage_yi_yong_kong_jian), Modifier.padding(start = 5.dp), fontSize = 14.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = CircleShape, color = appColor, modifier = Modifier.size(12.dp)) {

                }
                Text(stringResource(R.string.storage_app_zhan_yong_kong_jian), Modifier.padding(start = 5.dp), fontSize = 14.sp)
            }


        }
    }
}

