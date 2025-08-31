package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.domain.model.EpisodeCacheState
import com.imcys.bilibilias.core.domain.model.MediaStream
import com.imcys.bilibilias.logic.search.SearchComponent
import com.imcys.bilibilias.logic.search.SearchResultUiState
import com.imcys.bilibilias.logic.search.SelfInfoUiState
import com.imcys.bilibilias.ui.runtime.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun SearchScreen(
    component: SearchComponent,
    navigationToLogin: () -> Unit,
    navigationToPlayer: () -> Unit,
    navigationToSettings: () -> Unit,
) {
    val searchQuery by component.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState by component.searchResultUiState.collectAsStateWithLifecycle()
    val selfInfoUiState by component.selfInfoUiState.collectAsStateWithLifecycle()
    SearchContent(
        searchQuery = searchQuery,
        searchResultUiState = searchResultUiState,
        selfInfoUiState = selfInfoUiState,
        onSearchQueryChanged = component::onSearchQueryChanged,
        onLogout = component::onLogout,
        onCacheRequest = component::requestCache,
        navigationToLogin = navigationToLogin,
        navigationToPlayer = navigationToPlayer,
        navigationToSettings = navigationToSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    searchQuery: String,
    searchResultUiState: SearchResultUiState,
    selfInfoUiState: SelfInfoUiState,
    onSearchQueryChanged: (String) -> Unit,
    onLogout: () -> Unit,
    onCacheRequest: (request: EpisodeCacheRequest) -> Unit,
    navigationToLogin: () -> Unit,
    navigationToPlayer: () -> Unit,
    navigationToSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(navigationToSettings) {
                        Icon(Icons.Rounded.Settings, "Settings")
                    }
                    SelfAvatar(
                        selfInfoUiState,
                        modifier = Modifier,
                        onLoginClick = navigationToLogin,
                        onLogoutConfirmed = onLogout
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged
            )
            when (searchResultUiState) {
                SearchResultUiState.EmptyQuery -> {}
                SearchResultUiState.LoadFailed -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("LoadFailed")
                    }
                }

                SearchResultUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading")
                    }
                }

                is SearchResultUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(searchResultUiState.message)
                    }
                }

                is SearchResultUiState.Success -> {
                    val keyboardController = LocalSoftwareKeyboardController.current
                    LaunchedEffect(searchResultUiState) {
                        keyboardController?.hide()
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val resolutionOptions =
                            searchResultUiState.episodeCacheListState.videoStreams.distinctBy { it.id }
                        var selectedVideoOption by remember { mutableStateOf(searchResultUiState.episodeCacheListState.videoStreams[0]) }

                        val soundQualityOptions =
                            searchResultUiState.episodeCacheListState.audioStreams
                        var selectedAudioOption by remember { mutableStateOf(soundQualityOptions[0]) }
                        val requestCache: (episode: EpisodeCacheState) -> Unit =
                            { episodeCacheState ->
                                val request = EpisodeCacheRequest(
                                    episodeCacheState,
                                    videoQuality = selectedVideoOption.id,
                                    audioQuality = selectedAudioOption.id,
                                )
                                onCacheRequest(request)
                            }
                        QualitySelection(
                            videoStreams = resolutionOptions,
                            selectedVideoOption = selectedVideoOption,
                            onVideoOptionSelected = { selectedVideoOption = it },
                            audioStreams = soundQualityOptions,
                            selectedAudioOption = selectedAudioOption,
                            onAudioOptionSelected = { selectedAudioOption = it },
                        )
                        Text(
                            "分集(${searchResultUiState.episodes.size})",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        EpisodeList(searchResultUiState.episodes) {
                            requestCache(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QualitySelection(
    videoStreams: List<MediaStream>,
    audioStreams: List<MediaStream>,
    selectedVideoOption: MediaStream,
    onVideoOptionSelected: (MediaStream) -> Unit,
    selectedAudioOption: MediaStream,
    onAudioOptionSelected: (MediaStream) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (videoStreams.isNotEmpty()) {
            SelectField(
                label = { Text("画质") }, // Consider R.string.video_quality
                options = videoStreams,
                selectedOption = selectedVideoOption,
                onOptionSelected = onVideoOptionSelected,
                menuItemContent = { stream -> Text(stream.description) },
                optionToText = { stream -> stream.description },
                modifier = Modifier.weight(1f)
            )
        } else {
            // Optional: Show a placeholder or empty state if no video options
            Spacer(modifier = Modifier.weight(1f)) // To maintain layout
        }

        if (audioStreams.isNotEmpty()) {
            SelectField(
                label = { Text("音质") }, // Consider R.string.audio_quality
                options = audioStreams,
                selectedOption = selectedAudioOption,
                onOptionSelected = onAudioOptionSelected,
                menuItemContent = { stream -> Text(stream.description) },
                optionToText = { stream -> stream.description },
                modifier = Modifier.weight(1f)
            )
        } else {
            // Optional: Show a placeholder or empty state if no audio options
            Spacer(modifier = Modifier.weight(1f)) // To maintain layout
        }
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit = {},
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
