package com.imcys.bilibilias.ui.user.bangumifollow

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.network.model.user.BILIUserBangumiFollowInfo
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.ui.weight.tip.ASErrorTip
import com.imcys.bilibilias.weight.CommonError
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data class BangumiFollowRoute(
    val mid: Long = 0L
) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangumiFollowScreen(
    bangumiFollowRoute: BangumiFollowRoute,
    onToBack: () -> Unit
) {
    val vm = koinViewModel<BangumiFollowViewModel>()
    val items = vm.items.collectAsLazyPagingItems()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(bangumiFollowRoute.mid) {
        vm.initMid(bangumiFollowRoute.mid)
    }

    BangumiFollowScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = onToBack
    ) { paddingValues ->
        BangumiFollowContent(
            vm = vm,
            paddingValues = paddingValues,
            items
        )
    }


}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BangumiFollowContent(
    vm: BangumiFollowViewModel,
    paddingValues: PaddingValues,
    itemList: LazyPagingItems<BILIUserBangumiFollowInfo.ItemData>
) {

    LazyVerticalGrid(
        modifier = Modifier
            .padding(paddingValues)
            .padding(vertical = 5.dp, horizontal = 10.dp),
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(itemList.itemCount, key = {
            itemList[it]?.seasonId ?: it
        }) {
            itemList[it]?.let { item ->
                BangumiCard(
                    seasonId = item.seasonId,
                    title = item.title,
                    intro = item.evaluate,
                    updateInfo = item.newEp.indexShow ?: "",
                    seenInfo = item.progress,
                    pic = "${item.cover.toHttps()}@308w_410h_1c"
                )
            }

        }

        when (val state = itemList.loadState.refresh) {
            is LoadState.Error -> {
                item {
                    CommonError(errorMsg = "加载失败 \n ${state.error}", onRetry = {
                        itemList.refresh()
                    })
                }
            }

            is LoadState.Loading -> {
                bangumiCardListLoading()
            }

            else -> {}
        }

        when (val append = itemList.loadState.append) {
            LoadState.Loading -> item(span = { GridItemSpan(1) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ContainedLoadingIndicator()
                }
            }

            is LoadState.Error -> item(span = { GridItemSpan(1) }) {
                CommonError("加载失败 \n ${append.error}", onRetry = {
                    itemList.retry()
                })
            }

            else -> Unit
        }

    }
}


fun LazyGridScope.bangumiCardListLoading() {
    items(10) {
        BangumiCard(
            modifier = Modifier.shimmer(true),
            title = "标题",
            intro = "",
            updateInfo = "更新至X话",
            seenInfo = "看到",
            pic = "",
        )
    }
}

@Preview
@Composable
fun BangumiCard(
    modifier: Modifier = Modifier,
    seasonId: Long = 0L,
    title: String = "标题",
    intro: String = "介绍",
    updateInfo: String = "更新至X话",
    seenInfo: String = "看到",
    pic: String = "",
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = CardDefaults.shape,
        modifier = modifier.fillMaxWidth(),
        onClick = {
            sendAnalysisEvent(AnalysisEvent("ss${seasonId}"))
        }
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .aspectRatio(9f / 16f)
            ) {
                ASAsyncImage(
                    model = pic.toHttps(),
                    shape = CardDefaults.shape,
                    contentDescription = "番剧封面",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))
            Column(
                Modifier.weight(0.7f)
            ) {
                Text(text = title, fontSize = 18.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(text = intro, fontSize = 14.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)

                Spacer(Modifier.weight(1f))
                Text(text = seenInfo, fontSize = 14.sp)
                Text(text = updateInfo, fontSize = 14.sp)

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BangumiFollowScaffold(
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
                    Text(text = "追番")
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