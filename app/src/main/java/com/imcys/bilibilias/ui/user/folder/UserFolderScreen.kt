package com.imcys.bilibilias.ui.user.folder


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserFolderDetailInfo
import com.imcys.bilibilias.network.model.user.BILIUserFolderListInfo
import com.imcys.bilibilias.ui.weight.ASToggleButtonRowGroup
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.CommonError
import com.imcys.bilibilias.weight.UserWorkCard
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data class UserFolderRoute(
    val mid: Long,
) : NavKey


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFolderScreen(userFolderRoute: UserFolderRoute, onToBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val vm = koinViewModel<UserFolderViewModel>()
    val uiState by vm.uiState.collectAsState()
    val folderList by vm.folderList.collectAsState()
    val items = vm.items.collectAsLazyPagingItems()
    LaunchedEffect(userFolderRoute.mid) {
        vm.initMid(userFolderRoute.mid)
    }

    UserFolderScaffold(scrollBehavior, onToBack = onToBack) { paddingValues ->
        UserFolderContent(vm, uiState.currentMediaId, folderList, items, paddingValues)
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserFolderContent(
    vm: UserFolderViewModel,
    currentMediaId: Long,
    folderList: NetWorkResult<BILIUserFolderListInfo?>,
    itemList: LazyPagingItems<BILIUserFolderDetailInfo.Media>,
    paddingValues: PaddingValues
) {

    LazyVerticalGrid(
        modifier = Modifier
            .padding(paddingValues)
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        item(span = { GridItemSpan(2) }) {
            Surface(shape = CardDefaults.shape) {
                AsAutoError(folderList, onSuccessContent = {
                    folderList.data?.list?.let {
                        Column(Modifier.padding(5.dp)) {
                            ASToggleButtonRowGroup(
                                items = it,
                                itemsContent = { info -> Text(info.title) },
                                rule = { info -> currentMediaId == info.id },
                                key = { info -> info.id },
                                onCheckedChange = { item, checked ->
                                    vm.updateCurrentMediaId(item.id)
                                }
                            )
                        }
                    }
                })
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier)
        }


        items(itemList.itemCount, key = {
            itemList[it]?.id ?: it
        }) {
            itemList[it]?.let { item ->
                UserWorkCard(
                    modifier = Modifier.animateItem(),
                    bvId = item.bvid,
                    title = item.title,
                    pic = "${item.cover.toHttps()}@672w_378h_1c",
                    upName = item.upper.name,
                    mid = item.upper.mid,
                    view = item.cntInfo.play,
                    danmu = item.cntInfo.danmaku,
                )
            }

        }


        when (val state = itemList.loadState.refresh) {
            is LoadState.Error -> {
                item(span = { GridItemSpan(2) }) {
                    CommonError(errorMsg = stringResource(R.string.user_加载失), onRetry = {
                        itemList.refresh()
                    })
                }
            }

            is LoadState.Loading -> {
                userWorkCardListLoading()
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
                CommonError(stringResource(R.string.user_加载失_1), onRetry = {
                    itemList.retry()
                })
            }

            else -> Unit
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserFolderScaffold(
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
                    Text(text = stringResource(R.string.user_收藏))
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

fun LazyGridScope.userWorkCardListLoading() {
    items(10) {
        UserWorkCard(modifier = Modifier.shimmer(true))
    }
}