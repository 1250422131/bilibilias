package com.imcys.bilibilias.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

/**
 * 新增一个 Banner，最简单的情况下只需传入数据即可，如果需要更多样式请查看下面参数。
 * @param items 数据
 * @param onBannerClick Banner 点击事件的回调
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerPager(
    items: List<String>,
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit
) {
    if (items.isEmpty()) {
        throw NullPointerException("items is not null")
    }

    val pagerState = rememberPagerState(
        initialPage = (Int.MAX_VALUE / 2) % items.size,
    ) {
        Int.MAX_VALUE
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            key = { it },
            pageContent = { page ->
                val item = items[page % items.size]

                BannerCard(
                    item,
                    modifier = Modifier
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val offset =
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            val pageOffset = offset.absoluteValue

                            // We animate the scaleX + scaleY, between 85% and 100%
                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .padding(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    ratio = 2f,
                    contentScale = ContentScale.Crop,
                ) {
                    onBannerClick(item)
                }
            }
        )

        LaunchedEffect(pagerState) {
            try {
                while (true) {
                    delay(3000)
                    val current = pagerState.currentPage
                    val currentPos =
                        (current - (Int.MAX_VALUE / 2) % items.size).floorMod(items.size)
                    val nextPage = current + 1
                    val toPage =
                        if (nextPage < pagerState.pageCount) 0 else currentPos + (Int.MAX_VALUE / 2) % items.size + 1

                    if (toPage > current) {
                        pagerState.animateScrollToPage(toPage)
                    } else {
                        pagerState.scrollToPage(toPage)
                    }
                }
            } catch (e: CancellationException) {
                Napier.d(e, "page") { "Launched paging cancelled" }
            }
        }
    }
}

/**
 * Banner 图片展示卡片
 *
 * @param shape 图片圆角
 * @param contentScale 纵横比缩放
 * @param onClick Banner 图片点击事件
 */
@Composable
private fun BannerCard(
    data: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    contentScale: ContentScale,
    ratio: Float = 2f,
    onClick: () -> Unit,
) {
    Card(
        shape = shape,
        modifier = modifier
    ) {
        AsyncImage(
            model = data,
            contentDescription = "轮播图",
            modifier = Modifier
                .clickable(onClick = onClick)
                .aspectRatio(ratio),
            contentScale = contentScale
        )
    }
}

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}