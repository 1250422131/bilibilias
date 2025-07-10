package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.imcys.bilibilias.core.data.model.Quality
import com.imcys.bilibilias.logic.search.SearchComponent
import com.imcys.bilibilias.logic.search.SearchResultUiState
import com.imcys.bilibilias.ui.VideoDownloadDialog
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchScreen(component: SearchComponent) {
    val searchQuery by component.searchQuery.collectAsState()
    val searchResultUiState by component.searchResultUiState.collectAsState()
    SearchContent(
        searchQuery = searchQuery,
        searchResultUiState = searchResultUiState,
        onSearchQueryChanged = component::onSearchQueryChanged,
        onDownloadItemClick = component::downloadItem
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    searchQuery: String,
    searchResultUiState: SearchResultUiState,
    onSearchQueryChanged: (String) -> Unit,
    onDownloadItemClick: (Quality, String, Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    // todo user avatar
                    IconButton({}) {
                        Icon(Icons.Rounded.VerifiedUser, "Avatar")
                    }
                    IconButton({}) {
                        Icon(Icons.Rounded.Settings, "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        var dialog by rememberSaveable { mutableStateOf(false) }
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged
            ) {

            }
            when (searchResultUiState) {
                SearchResultUiState.EmptyQuery -> Text("EmptyQuery")
                SearchResultUiState.LoadFailed -> Text("LoadFailed")
                SearchResultUiState.Loading -> Text("Loading")
                is SearchResultUiState.Error -> Text(searchResultUiState.message)
                is SearchResultUiState.Success -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedCard(modifier = Modifier.padding(8.dp)) {
                        Row {
                            Box(modifier = Modifier.weight(3f)) {
                                AsyncImage(
                                    searchResultUiState.episode.cover,
                                    "VideoCover",
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.Inside,
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                                    .height(IntrinsicSize.Min),
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                TextButton(
                                    { searchResultUiState.episode.bvid },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("看视频")
                                }
                                TextButton(
                                    { searchResultUiState.episode.owner.id },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("去主页")
                                }
                            }
                        }
                    }
                    Button(
                        { dialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("下载")
                    }
                    VideoDownloadDialog(
                        dialog,
                        searchResultUiState.episode.series,
                        searchResultUiState.episode.qualities,
                        onDismiss = { dialog = false },
                        onClick = { quality, cid ->
                            onDownloadItemClick(quality, searchResultUiState.episode.bvid, cid)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "search",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "close",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    if (searchQuery.isBlank()) return@onKeyEvent false
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.isBlank()) return@KeyboardActions
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
}

@Preview
@Composable
fun PreviewContent() {
//    SearchContent(
//        "haha",
//        SearchResultUiState.Success(
//            aid = 0,
//            bvid = "",
//            desc = "",
//            cover = "",
//            title = "title",
//            ownerId = 0,
//            ownerFace = "",
//            ownerName = "name"
//        ),
//        onSearchQueryChanged = {},
//        onDownloadItemClick = { _, _, _ -> }
//    )
}
