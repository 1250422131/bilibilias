package com.imbys.bilibilias.feature.authorspace

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.imcys.bilibilias.core.ui.UnitedDetails

@Composable
fun AuthorSpaceContent(component: AuthorSpaceComponent) {
    val lazyPagingItems = component.flow.collectAsLazyPagingItems()
    AuthorSpaceScreen(lazyPagingItems)
}

@Composable
private fun AuthorSpaceScreen(lazyPagingItems: LazyPagingItems<UnitedDetails>) {
    LazyColumn {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = "Waiting for items to load from the backend",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
        items(count = lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            if (item != null) {
                UnitedDetails(item)
            }
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


