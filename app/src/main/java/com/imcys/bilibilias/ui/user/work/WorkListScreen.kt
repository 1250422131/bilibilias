package com.imcys.bilibilias.ui.user.work

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsCardTextField
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.WorkCard
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class WorkListRoute(
    val mid: Long = 0L
) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WorkListScreen(
    workListRoute: WorkListRoute,
    onToBack: () -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val vm = koinViewModel<WorkListViewModel>()
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(workListRoute.mid) {
        vm.init(mid = workListRoute.mid)
    }

    WorkListScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = onToBack
    ) { paddingValues ->
        WorkListScreenContent(
            vm = vm,
            uiState = uiState,
            paddingValues = paddingValues,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkListScreenContent(
    vm: WorkListViewModel,
    uiState: WorkListViewModel.UIState,
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        when {
            uiState.isRefreshing && uiState.items.isEmpty() -> {
                InitLoadingWorkList()
            }

            else -> {
                WorkList(
                    uiState,
                    onLoadMore = { vm.loadNextPage() },
                    onUpdateKeyword = { vm.onQueryChange(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WorkList(
    uiState: WorkListViewModel.UIState,
    onLoadMore: () -> Unit,
    onUpdateKeyword: (String?) -> Unit,
) {

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState, uiState.hasMore, uiState.isAppending, uiState.isRefreshing) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo }.collect { visibleItems ->
            val totalItemsCount = gridState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: -1
            val isNotLoading = !uiState.isAppending && !uiState.isRefreshing
            if (isNotLoading && uiState.hasMore && lastVisibleItemIndex >= totalItemsCount - 1
            ) {
                onLoadMore.invoke()
            }
        }
    }

    LazyVerticalGrid(
        state = gridState,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        item(
            span = { GridItemSpan(2) }
        ) {
            AsCardTextField(
                hint = "搜索投稿",
                autoFocus = false, value = uiState.query, onValueChange = onUpdateKeyword,
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), modifier = Modifier.animateItem())
        }

        items(uiState.items, key = { it.bvid }) {
            WorkCard(
                modifier = Modifier.animateItem(),
                bvId = it.bvid,
                title = it.title,
                pic = "${it.pic.toHttps()}@672w_378h_1c.webp",
                view = it.play,
                danmu = it.danmu,
            )
        }

        if (uiState.isAppending) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ContainedLoadingIndicator()
                }
            }
        }

    }
}

@Composable
private fun InitLoadingWorkList() {
    LazyVerticalGrid(
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Fixed(2),
    ) {
        items(10) {
            WorkCard(modifier = Modifier.shimmer(true))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkListScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = {
                    Text(text = "投稿列表")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onToBack.invoke()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "返回",
                        )
                    }
                }
            )
        },
    ) {
        content(it)
    }
}