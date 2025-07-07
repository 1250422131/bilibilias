package com.imcys.bilibilias.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Stable
class BezierShapes(
    private var circleRadius: Float = 30f,
    private var circleMarginTop: Float = 80f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val rect = Rect(Offset(0f, 0f), Offset(size.width, size.height))
        path.addRoundRect(
            RoundRect(
                rect,
                CornerRadius(circleRadius),
                CornerRadius(circleRadius),
                CornerRadius(circleRadius),
                CornerRadius(circleRadius)
            )
        )
        val circleLeftPath = Path()
        val circleRightPath = Path()
        val circleLeftRect = Rect(Offset(0f, size.height / 2 + circleMarginTop), circleRadius)
        val circleRightRect =
            Rect(Offset(size.width, size.height / 2 + circleMarginTop), circleRadius)
        circleLeftPath.addArc(circleLeftRect, 0f, 360f)
        circleRightPath.addArc(circleRightRect, 0f, 360f)
        path.op(path, circleLeftPath, PathOperation.Difference)
        path.op(path, circleRightPath, PathOperation.Difference)
        path.close()
        return Outline.Generic(path)
    }
}

@Preview(widthDp = 480, heightDp = 200)
@Composable
fun CardComposeView(navigateToScreen: (route: String) -> Unit = {}) {
    Box {
//        Image(
//            painter = painterResource(R.mipmap.ticket_bar_bg),
//            contentDescription = "",
//            modifier = Modifier
//                .fillMaxSize()
//                .fillMaxWidth()
//                .blur(10.dp),
//            contentScale = ContentScale.Crop
//        )
        Box(
            Modifier
                .background(Color.Transparent)
                .padding(10.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(BezierShapes())
                    .shadow(2.dp, spotColor = Color.Black, ambientColor = Color.Black, clip = true)
                    .border(1.4.dp, Color(224, 166, 83, 255), BezierShapes())
                    .background(Color(238, 220, 203, 255))
                    .drawBehind {
                        drawLine(
                            Color(171, 90, 3, 255),
                            Offset(0f, size.height / 2f + 80),
                            Offset(size.width, size.height / 2f + 80),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(11f, 11f), 0.6f)
                        )
                    }
                    .padding(10.dp)

            ) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        "$12 OFF",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        "Available for orders over $39",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp, bottom = 10.dp, top = 6.dp)
                    )
                    Text(
                        "06/25/2023 09:00-07/01/2023 09:00",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                    Box(Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(end = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 10.dp)
                                .background(
                                    Color(239, 230, 216, 255),
                                    shape = RoundedCornerShape(5.dp)
                                )
                        ) {
                            Text(
                                "Code:",
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp).padding(vertical = 8.dp)
                            )
                            Text(
                                "DsCT12",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                                    .padding(top = 8.dp, end = 10.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 10.dp)
                                .background(
                                    Color(134, 56, 4, 255),
                                    shape = RoundedCornerShape(5.dp)
                                )
                        ) {
                            Text(
                                "COPY",
                                color = Color.White,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }

                }
            }
        }
    }
}