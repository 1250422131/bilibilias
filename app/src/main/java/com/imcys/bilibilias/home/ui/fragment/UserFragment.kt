package com.imcys.bilibilias.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Collections
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.activity.user.CollectionActivity
import com.imcys.bilibilias.home.ui.viewmodel.UserSpaceUiState
import com.imcys.bilibilias.home.ui.viewmodel.UserSpaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class UserFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    UserSpaceScreen()
                }
            }
        }
    }

    @Composable
    fun UserSpaceScreen(viewModel: UserSpaceViewModel = hiltViewModel()) {
        val uiState by viewModel.userSpaceUiState.collectAsStateWithLifecycle()
        UserSpaceContent(uiState, loadMore = viewModel::getHistory)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserSpaceContent(
        uiState: UserSpaceUiState,
        loadMore: () -> Unit
    ) {
        val context = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        ) {
                                AsyncImage(uiState.face, null, contentScale = ContentScale.Crop,)
                        }
                    },
                    actions = {
                        IconButton(
                            {
                                CollectionActivity.actionStart(context)
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Collections, null,
                                tint = colorResource(R.color.color_primary)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            val lazyListState = rememberLazyListState()
            val reachedBottom by remember { derivedStateOf { lazyListState.reachedBottom() } }
            LaunchedEffect(Unit) {
                snapshotFlow { reachedBottom }
                    .filter { it }
                    .collect { loadMore() }
            }
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                state = lazyListState
            ) {
                items(uiState.histories, key = { it.bvid }) { item ->
                    ListItem(
                        headlineContent = {
                            Text(text = item.title, maxLines = 2)
                        },
                        supportingContent = {
                            Text(text = item.author)
                        },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(128.dp, 72.dp)
                            ) {
                                AsyncImage(item.cover, null, contentScale = ContentScale.Crop)
                            }
                        },
                        modifier = Modifier
                            .animateItem()
                            .clickable {
                                val intent = Intent(context, AsVideoActivity::class.java).apply {
                                    putExtra("bvId", item.bvid)
                                }
                                context.startActivity(intent)
                            }
                    )
                }
            }
        }
    }

    internal fun LazyListState.reachedBottom(buffer: Int = 3): Boolean {
        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
        return lastVisibleItem?.index != 0 && lastVisibleItem?.index == layoutInfo.totalItemsCount - buffer
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "UserFragment")
    }

    override fun onResume() {
        super.onResume()
        StatService.onPageStart(context, "UserFragment")
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserFragment()
    }
}
