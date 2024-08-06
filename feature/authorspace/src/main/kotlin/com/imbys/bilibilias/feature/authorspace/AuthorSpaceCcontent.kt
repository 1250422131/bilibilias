package com.imbys.bilibilias.feature.authorspace

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.imcys.bilibilias.core.ui.UnitedDetails

@Composable
fun AuthorSpaceContent(component: AuthorSpaceComponent) {
    val lazyPagingItems = component.flow.collectAsLazyPagingItems()
    val model by component.models.collectAsStateWithLifecycle()
    AuthorSpaceScreen(lazyPagingItems, onEvent = component::take)
}

@Composable
private fun AuthorSpaceScreen(
    lazyPagingItems: LazyPagingItems<UnitedDetails>,
    onEvent: (AuthorSpaceEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            IconButton(onClick = { onEvent(AuthorSpaceEvent.DownloadAll) }) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null)
            }
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                item {
                    Text(
                        text = "Waiting for items to load from the backend",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                }
            }
            items(count = lazyPagingItems.itemCount) { index ->
                val item = lazyPagingItems[index]
                if (item != null) {
                    UnitedDetails(item) {
                        onEvent(AuthorSpaceEvent.ChangeSelection(it))
                    }
                }
            }

            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                }
            }
        }
    }
}
