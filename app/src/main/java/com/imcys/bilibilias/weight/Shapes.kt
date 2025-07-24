package com.imcys.bilibilias.weight

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Rect

val EllipseShape = GenericShape { size, _ ->
    // 直接画一个椭圆路径
    addOval(Rect(0f, 0f, size.width, size.height))
}