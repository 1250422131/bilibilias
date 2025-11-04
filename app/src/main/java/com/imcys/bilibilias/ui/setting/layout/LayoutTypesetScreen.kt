package com.imcys.bilibilias.ui.setting.layout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.data.repository.getDescription
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.ASAnimatedContent
import com.imcys.bilibilias.weight.maybeNestedScroll
import com.imcys.bilibilias.weight.reorderable.ItemPosition
import com.imcys.bilibilias.weight.reorderable.ReorderableItem
import com.imcys.bilibilias.weight.reorderable.detectReorderAfterLongPress
import com.imcys.bilibilias.weight.reorderable.rememberReorderableLazyListState
import com.imcys.bilibilias.weight.reorderable.reorderable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
object LayoutTypesetRoute : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutTypesetScreen(layoutTypesetRoute: LayoutTypesetRoute, onToBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val vm = koinViewModel<LayoutTypesetViewModel>()
    val homeLayoutList by vm.homeLayoutTypesetList.collectAsState()
    LayoutTypesetScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = onToBack
    ) {
        LayoutTypesetContent(
            modifier = Modifier
                .maybeNestedScroll(scrollBehavior)
                .padding(it),
            vm = vm,
            homeLayoutList, onMove = { from, to ->
                vm.moveLayoutItem(from.index, to.index)
            })
    }
}

@Composable
fun LayoutTypesetContent(
    modifier: Modifier = Modifier,
    vm: LayoutTypesetViewModel,
    homeLayoutList: List<AppSettings.HomeLayoutItem>,
    onMove: (ItemPosition, ItemPosition) -> Unit,
) {
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        onMove(from, to)
    })
    val haptics = LocalHapticFeedback.current

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .reorderable(state)
            .detectReorderAfterLongPress(state),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(homeLayoutList, { it.type }) { item ->
            ReorderableItem(state, key = item.type) { isDragging ->
                val hasTriggered = remember { mutableStateOf(false) }

                LaunchedEffect(isDragging) {
                    if (isDragging && !hasTriggered.value) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        hasTriggered.value = true
                    } else if (!isDragging) {
                        // 重置状态，允许下次拖动再次触发
                        hasTriggered.value = false
                    }
                }

                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardDefaults.shape)
                        .shadow(elevation.value)
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.type.getDescription(), modifier = Modifier,
                        )
                        Spacer(Modifier.weight(1f))
                        ASIconButton(onClick = {
                            vm.setLayoutItemHidden(item, !item.isHidden)
                        }) {
                            Icon(
                                if (item.isHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = if (item.isHidden) stringResource(R.string.home_text_4116) else stringResource(R.string.home_text_8598),
                            )
                        }
                        ASIconButton(onClick = {}) {
                            Icon(Icons.Outlined.Menu, contentDescription = stringResource(R.string.home_text_804))
                        }
                    }

                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutTypesetScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = { Text(text = stringResource(R.string.home_text_3660)) },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                },
                alwaysDisplay = false
            )
        },
    ) {
        content(it)
    }

}