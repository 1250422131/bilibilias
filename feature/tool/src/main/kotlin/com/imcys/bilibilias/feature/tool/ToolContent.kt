package com.imcys.bilibilias.feature.tool

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallToAction
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.designsystem.theme.AsTheme
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.download.Format
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.model.video.ViewIds
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.ui.radio.CodecsRadioGroup
import com.imcys.bilibilias.core.ui.radio.FileTypeRadioGroup
import com.imcys.bilibilias.core.ui.radio.rememberCodecsState
import com.imcys.bilibilias.core.ui.radio.rememberFileTypeState
import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetScreen
import com.imcys.bilibilias.feature.tool.R as searchR

@Composable
fun ToolContent(
    component: ToolComponent,
    navigationToSettings: () -> Unit,
    navigationToAuthorSpace: (Mid) -> Unit,
    navigationToFfmpegAction: () -> Unit,
) {
    val searchQuery by component.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState by component.searchResultUiState.collectAsStateWithLifecycle()
    ToolContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = component::onSearchQueryChanged,
        searchResultUiState = searchResultUiState,
        onDownload = component::download,
        navigationToSettings = navigationToSettings,
        navigationToAuthorSpace = navigationToAuthorSpace,
        navigationToFfmpegAction = navigationToFfmpegAction,
        navigationToDownloadTypeBottomSheet = component::navigationToDownloadTypeBottomSheet,
    )
    val dialogSlot by component.dialogSlot.subscribeAsState()
    dialogSlot.child?.instance?.also {
        DownloadBottomSheetScreen(component = it, onDismiss = component::onDismissClicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ToolContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    searchResultUiState: SearchResultUiState,
    onDownload: (DownloadRequest) -> Unit,
    navigationToSettings: () -> Unit,
    navigationToAuthorSpace: (Mid) -> Unit,
    navigationToFfmpegAction: () -> Unit,
    navigationToDownloadTypeBottomSheet: (String, String, ViewIds) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = navigationToFfmpegAction) {
                        Icon(
                            Icons.Default.CallToAction,
                            contentDescription = "action",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    IconButton(onClick = navigationToSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "设置",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
            )

            when (searchResultUiState) {
                SearchResultUiState.EmptyQuery -> Unit
                SearchResultUiState.LoadFailed -> EmptySearchResultBody(searchQuery) {}
                SearchResultUiState.Loading -> Unit
                is SearchResultUiState.Success -> {
                    SearchResultBody(
                        aid = searchResultUiState.aid,
                        bvid = searchResultUiState.bvid,
                        cid = searchResultUiState.cid,
                        mid = searchResultUiState.mid,
                        collection = searchResultUiState.collection,
                        ownerFace = searchResultUiState.ownerFace,
                        onDownload = onDownload,
                        navigationToAuthorSpace = navigationToAuthorSpace,
                        navigationToDownloadTypeBottomSheet = navigationToDownloadTypeBottomSheet,
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultBody(
    aid: Aid,
    bvid: Bvid,
    cid: Cid,
    mid: Mid,
    collection: List<View>,
    ownerFace: String,
    onDownload: (DownloadRequest) -> Unit,
    navigationToAuthorSpace: (Mid) -> Unit,
    navigationToDownloadTypeBottomSheet: (String, String, ViewIds) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(4.dp),
        ) {
            item {
                AsyncImage(
                    model = ownerFace,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .clickable { navigationToAuthorSpace(mid) },
                )
            }
            items(collection, key = { it.cid }) { item ->
                ViewItem(item.title, item.videoStreamDesc) {
                    onDownload(
                        DownloadRequest(
                            ViewInfo(
                                aid,
                                bvid,
                                item.cid,
                                item.title,
                            ),
                            it,
                        ),
                    )
                }
            }
            item {
                AsButton(
                    onClick = {
                        navigationToDownloadTypeBottomSheet(
                            "title",
                            "author",
                            ViewIds(aid, bvid, cid),
                        )
                    },
                ) {
                    Text("Test Sheet")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewItem(
    title: String,
    streamDesc: VideoStreamDesc,
    onDownload: (Format) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = title, modifier = Modifier.basicMarquee())
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }
        AnimatedVisibility(visible = expanded) {
            val codecsState = rememberCodecsState()
            val typeState = rememberFileTypeState()
            var currentQuality by remember { mutableStateOf(streamDesc.descriptionQuality.first()) }
            Column {
                LazyRow {
                    items(streamDesc.descriptionQuality) { item ->
                        TextButton(
                            onClick = { currentQuality = item },
                            border = if (currentQuality == item) {
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else {
                                null
                            },
                        ) {
                            Text(text = item.desc)
                        }
                    }
                }
                CodecsRadioGroup(codecsState)
                FileTypeRadioGroup(typeState)
                AsTextButton(
                    onClick = {
                        onDownload(
                            Format(
                                codecsState.current.codeid,
                                typeState.current,
                                currentQuality.quality,
                            ),
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "下载")
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(
                    id = searchR.string.feature_tool_title,
                ),
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
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(
                            id = searchR.string.feature_tool_clear_search_text_content_desc,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
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
                onSearchExplicitlyTriggered()
            },
        ),
        label = { Text(stringResource(searchR.string.feature_tool_label)) },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun SearchNotReadyBody() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 48.dp),
    ) {
        Text(
            text = stringResource(id = searchR.string.feature_tool_not_ready),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp),
        )
    }
}

@Composable
fun EmptySearchResultBody(
    searchQuery: String,
    onInterestsClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 48.dp),
    ) {
        val message = stringResource(id = searchR.string.feature_tool_result_not_found, searchQuery)
        val start = message.indexOf(searchQuery)
        Text(
            text = AnnotatedString(
                text = message,
                spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        start = start,
                        end = start + searchQuery.length,
                    ),
                ),
            ),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp),
        )
        val tryAnotherSearchString = buildAnnotatedString {
            append(stringResource(id = searchR.string.feature_tool_try_another_search))
            append(" ")
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                ),
            ) {}
            append(" ")
        }
        ClickableText(
            text = tryAnotherSearchString,
            style = MaterialTheme.typography.bodyLarge.merge(
                TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                ),
            ),
            modifier = Modifier
                .padding(start = 36.dp, end = 36.dp, bottom = 24.dp)
                .clickable {},
        ) { offset ->
            tryAnotherSearchString.getStringAnnotations(start = offset, end = offset)
                .firstOrNull()
                ?.let { onInterestsClick() }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchNotReadyBodyPreview() {
    AsTheme {
        SearchNotReadyBody()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptySearchResultColumnPreview() {
    AsTheme {
        EmptySearchResultBody(
            onInterestsClick = {},
            searchQuery = "C++",
        )
    }
}
