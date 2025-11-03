package com.imcys.bilibilias.ui.user.history


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.CommonError
import com.imcys.bilibilias.weight.HistoryPlayVideoCard
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data object UserPlayHistoryRoute : NavKey


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPlayHistoryScreen(userPlayHistoryRoute: UserPlayHistoryRoute, onToBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val vm = koinViewModel<UserPlayHistoryViewModel>()
    UserPlayHistoryScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = onToBack
    ) {
        UserPlayHistoryContent(vm, it)
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserPlayHistoryContent(vm: UserPlayHistoryViewModel, paddingValues: PaddingValues) {

    val itemList = vm.items.collectAsLazyPagingItems()


    LazyVerticalGrid(
        modifier = Modifier
            .padding(paddingValues)
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {


        items(itemList.itemCount, key = { index ->
            val item = itemList[index]
            item?.history?.bvid?.ifBlank { "empty_$index" } ?: "empty_$index"
        }) { index ->
            itemList[index]?.let { item ->
                HistoryPlayVideoCard(
                    modifier = Modifier.animateItem(),
                    bvId = item.history.bvid,
                    title = item.title,
                    pic = "${item.cover.toHttps()}@672w_378h_1c",
                    upName = item.authorName,
                    mid = item.authorMid,
                    duration = item.duration,
                    progress = item.progress,
                )
            }

        }


        when (val state = itemList.loadState.refresh) {
            is LoadState.Error -> {
                item(span = { GridItemSpan(2) }) {
                    CommonError(errorMsg = stringResource(R.string.user_jia_zai_shi_bai_nstateerr), onRetry = {
                        itemList.refresh()
                    })
                }
            }

            is LoadState.Loading -> {
                historyPlayCardListLoading()
            }

            else -> {}
        }

        when (val append = itemList.loadState.append) {
            LoadState.Loading -> item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ContainedLoadingIndicator()
                }
            }

            is LoadState.Error -> item(span = { GridItemSpan(2) }) {
                CommonError(stringResource(R.string.user_jia_zai_shi_bai_nappender), onRetry = {
                    itemList.retry()
                })
            }

            else -> Unit
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserPlayHistoryScaffold(
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
                    Text(text = stringResource(R.string.user_zui_jin_bo_fang))
                },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                }
            )
        },
    ) {
        content(it)
    }
}

fun LazyGridScope.historyPlayCardListLoading() {
    items(10) {
        HistoryPlayVideoCard(modifier = Modifier.shimmer(true))
    }
}