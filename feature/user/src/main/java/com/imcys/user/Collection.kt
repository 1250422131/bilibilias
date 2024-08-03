package com.imcys.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.imcys.designsystem.component.FullScreenScaffold
import com.imcys.designsystem.component.IconArrowBackButton
import com.imcys.model.UserCreateCollectionBean
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Collection(userViewModel: UserViewModel) {
    val navHostController = rememberNavController()
    LaunchedEffect(Unit) {
        userViewModel.loadCollectionList()
    }
    FullScreenScaffold(
        Modifier.padding(horizontal = 20.dp),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconArrowBackButton {
                        navHostController.navigateUp()
                    }
                }
            )
        }
    ) { innerPadding ->
        val userStateState by userViewModel.userDataState.collectAsStateWithLifecycle()
        var page by remember { mutableIntStateOf(0) }
        Column(Modifier.padding(innerPadding)) {
            TabRow(userStateState.collectionList) { id, page ->
                userViewModel.loadCollectionData(id, page)
            }
            val gridState = rememberLazyStaggeredGridState()
            LaunchedEffect(gridState.canScrollBackward) {
                if (!gridState.canScrollBackward) {
                    page++
                }
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                Modifier.padding(innerPadding),
                state = gridState
            ) {
                items(userStateState.collectionList) {
                    Text(it.title)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabRow(collections: ImmutableList<UserCreateCollectionBean.Collection>, onClick: (Int, Int) -> Unit) {
    if (collections.isEmpty()) return

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var currentPage by remember { mutableIntStateOf(0) }
    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
        collections.forEachIndexed { index, collection ->
            Card(
                onClick = {
                    selectedTabIndex = index
                    currentPage = 0
                    onClick(collection.id, currentPage)
                },
                Modifier.padding(horizontal = 8.dp)
            ) {
                Tab(
                    selected = selectedTabIndex == index,
                    {},
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                ) {
                    Text(
                        collection.title,
                        Modifier.padding(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ShowTime() {
    Text(text = "")
}
