package com.imcys.bilibilias.ui.user.like


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserVideoLikeInfo
import com.imcys.bilibilias.ui.user.folder.userWorkCardListLoading
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.UserWorkCard
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class LikeVideoRoute(val mid: Long,val type: LikePageType) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeVideoScreen(likeVideoRoute: LikeVideoRoute, onToBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val vm = koinViewModel<LikeVideoViewModel>()
    val likeVideoList by vm.likeVideoList.collectAsState()

    LaunchedEffect(likeVideoRoute.mid,likeVideoRoute.type) {
        vm.initMid(likeVideoRoute.mid,likeVideoRoute.type)
    }

    LikeVideoScaffold(likeVideoRoute.type,scrollBehavior, onToBack = onToBack) { paddingValues ->
        LikeVideoContent(
            vm,
            likeVideoList,
            paddingValues,
        )
    }
}

@Composable
fun LikeVideoContent(
    vm: LikeVideoViewModel,
    likeVideoList: NetWorkResult<BILIUserVideoLikeInfo?>,
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

        if (likeVideoList.status == ApiStatus.LOADING) {
            userWorkCardListLoading()
        }

        if (likeVideoList.status == ApiStatus.ERROR) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                AsAutoError(likeVideoList)
            }
        }

        items(likeVideoList.data?.list ?: emptyList(), key = { it.cid ?: it.bvid}) { item ->
            UserWorkCard(
                modifier = Modifier.animateItem(),
                bvId = item.bvid,
                title = item.title,
                pic = "${item.pic.toHttps()}@672w_378h_1c",
                upName = item.owner.name,
                mid = item.owner.mid,
                view = item.stat.view,
                danmu = item.stat.danmaku,
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LikeVideoScaffold(
    pageType: LikePageType,
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
                    Text(text = when(pageType){
                        LikePageType.LIKE -> stringResource(R.string.user_text_1)
                        LikePageType.COIN -> stringResource(R.string.app_text_14)
                    })
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