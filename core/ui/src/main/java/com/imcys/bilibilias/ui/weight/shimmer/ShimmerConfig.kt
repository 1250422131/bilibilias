package com.imcys.bilibilias.ui.weight.shimmer


import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color

data class ShimmerConfig(
    // 未高亮部分颜色
    val contentColor: Color = Color.LightGray.copy(alpha = 0.3f),
    // 高亮部分颜色
    val higLightColor: Color = Color.LightGray.copy(alpha = 0.9f),
    // 渐变部分宽度
    @FloatRange(from = 0.0, to = 1.0)
    val dropOff: Float = 0.5f,
    // 高亮部分宽度
    @FloatRange(from = 0.0, to = 1.0)
    val intensity: Float = 0.2f,
    //骨架屏动画方向
    val direction: ShimmerDirection = ShimmerDirection.LeftToRight,
    //动画旋转角度
    val angle: Float = 20f,
    //动画时长
    val duration: Float = 1000f,
    //两次动画间隔
    val delay: Float = 200f
)