package com.imcys.bilibilias.ui.weight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <T> ASHorizontalMultiBrowseCarousel(
    items: List<T>,
    modifier: Modifier = Modifier,
    autoScroll: Boolean = false,
    scrollTime: Long = 5000L,
    crossinline itemsContent: @Composable CarouselItemScope.(item: T) -> Unit,
) {
    val carouselState = rememberCarouselState { items.size }
    var isUserInteracting by remember { mutableStateOf(false) }
    var autoScrollKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(autoScroll, autoScrollKey) {
        if (autoScroll) {
            while (!isUserInteracting) {
                delay(scrollTime)
                if (carouselState.currentItem == items.size - 1) {
                    carouselState.scrollToItem(0)
                } else {
                    carouselState.animateScrollToItem(carouselState.currentItem + 1)
                }
            }
        }
    }

    HorizontalMultiBrowseCarousel(
        state = carouselState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .pointerInput(Unit) {
                // 监听触摸事件
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Press -> {
                                isUserInteracting = true
                            }
                            PointerEventType.Release,
                            PointerEventType.Exit -> {
                                isUserInteracting = false
                                autoScrollKey++ // 触发重启自动滚动
                            }

                            else -> {}
                        }
                    }
                }
            },
        preferredItemWidth = 300.dp,
        itemSpacing = 8.dp,
    ) { i ->
        val item = items[i]
        itemsContent(item)
    }
}