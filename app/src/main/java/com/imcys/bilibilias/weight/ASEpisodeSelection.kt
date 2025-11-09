package com.imcys.bilibilias.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.imcys.bilibilias.R
import com.imcys.bilibilias.datastore.AppSettings
import kotlin.math.ceil

private const val PAGE_SIZE = 12


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T, R> ASSectionEpisodeSelection(
    modifier: Modifier = Modifier,
    sectionList: List<R>? = null,
    episodeList: List<T>?,
    sectionChecked: (R) -> Boolean = { false },
    episodeSelected: (T) -> Boolean,
    episodeEnabled: (T) -> Boolean = { true },
    sectionTitle: (R) -> String = { "" },
    episodeTitle: (T) -> String,
    episodeListMode : AppSettings.EpisodeListMode = AppSettings.EpisodeListMode.EpisodeListMode_Grid,
    onUpdateSelected: (T) -> Unit,
    onSelectSection: (R) -> Unit = {},
    episodeKeyOf: ((T) -> Any)? = null,
    episodeContentContainer: @Composable (T, content: @Composable () -> Unit) -> Unit = { data, content ->
        content()
    },
) {
    val haptics = LocalHapticFeedback.current

    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(
                sectionList ?: emptyList(),
                key = { it.hashCode() }
            ) { info ->
                ToggleButton(
                    checked = sectionChecked(info),
                    onCheckedChange = {
                        if (it) {
                            haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            onSelectSection.invoke(info)
                        }
                    },
                ) {
                    Text(sectionTitle.invoke(info))
                }
            }
        }

        ASEpisodeSelection(
            modifier = modifier,
            episodeList = episodeList,
            episodeSelected = episodeSelected,
            episodeEnabled = episodeEnabled,
            episodeTitle = episodeTitle,
            episodeListMode = episodeListMode,
            onUpdateEpisodeSelected = onUpdateSelected,
            episodeKeyOf = episodeKeyOf,
            episodeContentContainer = episodeContentContainer,
        )
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> ASEpisodeSelection(
    modifier: Modifier = Modifier,
    episodeList: List<T>?,
    episodeSelected: (T) -> Boolean,
    episodeEnabled: (T) -> Boolean = { true },
    episodeTitle: (T) -> String,
    episodeListMode : AppSettings.EpisodeListMode = AppSettings.EpisodeListMode.EpisodeListMode_List,
    onUpdateEpisodeSelected: (T) -> Unit,
    episodeKeyOf: ((T) -> Any)? = null,
    episodeContentContainer: @Composable (T, content: @Composable () -> Unit) -> Unit = { data, content ->
        content()
    },
) {
    var currentPageListIndex by remember(episodeList) { mutableIntStateOf(0) }

    val list = remember(episodeList) { episodeList ?: emptyList() }
    val episodeCount = remember(list) { list.size }
    val pageCount = remember(episodeCount) {
        if (episodeCount == 0) 0 else ceil(episodeCount / PAGE_SIZE.toDouble()).toInt()
    }
    val maxPageIndex = if (pageCount == 0) 0 else pageCount - 1
    val safePageIndex = currentPageListIndex.coerceIn(0, maxPageIndex)

    val startIndex = safePageIndex * PAGE_SIZE
    val pageItems = remember(list, safePageIndex) {
        val endIndex = minOf(startIndex + PAGE_SIZE, list.size)
        list.subList(startIndex, endIndex)
    }


    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        items(
            pageCount,
            key = { "p_$it" }
        ) { index ->
            val startEp = index * PAGE_SIZE + 1
            val endEp = minOf((index + 1) * PAGE_SIZE, episodeCount)
            FilterChip(
                onClick = { currentPageListIndex = index },
                label = { Text(stringResource(R.string.analysis_episode_range_format, startEp, endEp)) },
                selected = index == safePageIndex,
            )
        }
    }

    val isList = episodeListMode == AppSettings.EpisodeListMode.EpisodeListMode_List
    val showRowNumber = if (isList) 3 else 2
    LazyVerticalGrid(
        if (isList) GridCells.Fixed(1) else GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier.sizeIn(maxHeight = (60 * showRowNumber + showRowNumber * 10).dp)
    ) {
        itemsIndexed(
            items = pageItems,
            key = { idx, item -> episodeKeyOf?.invoke(item) ?: (startIndex + idx) }
        ) { _, item ->
            episodeContentContainer(item) {
                FilterChip(
                    enabled = episodeEnabled(item),
                    selected = episodeSelected(item),
                    onClick = {
                        onUpdateEpisodeSelected.invoke(item)
                    },
                    label = {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                episodeTitle(item),
                                maxLines = 2,
                                fontSize = 14.sp,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                )
            }
        }
    }
}