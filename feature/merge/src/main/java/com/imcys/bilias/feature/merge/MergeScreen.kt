package com.imcys.bilias.feature.merge

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hjq.toast.Toaster
import com.imcys.model.download.Entry
import java.util.Locale

const val BASE_PATH = "/storage/emulated/0/Android/data/"
const val BILI_PATH = "tv.danmaku.bili/download"
const val BILI_FULL_PATH = BASE_PATH + BILI_PATH

@Composable
internal fun MergeRoute() {
    val viewModel: MergeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MergeScreen(
        uiState = uiState,
        mixVideoAudio = viewModel::mixVideoAudio
    )
}

@Composable
internal fun MergeScreen(
    uiState: UiState,
    mixVideoAudio: (List<Entry>, Context) -> Unit
) {
    val selectedList = remember { mutableStateListOf<Entry>() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                actions = {
                    val context = LocalContext.current
                    TextButton(
                        onClick = { mixVideoAudio(selectedList, context) },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "合并")
                    }
                }
            )
        }
    ) { paddingValues ->
        LaunchedEffect(uiState.errorMessage, uiState.mixMessage) {
            if (uiState.errorMessage.isNotEmpty()) {
                Toaster.show(uiState.errorMessage)
            }
            if (uiState.mixMessage.isNotEmpty()) {
                Toaster.show(uiState.mixMessage)
            }
        }

//        if (uiState.startMix) {
//            Dialog(onDismissRequest = { }) {
//                Column(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 16.dp)
//                            .animateContentSize(),
//                    ) {
//                        Column {
//                            LinearProgressIndicator(
//                                uiState.progress / 100f,
//                                color = MaterialTheme.colorScheme.tertiary
//                            )
//                            Text(text = uiState.current)
//                        }
//                    }
//                }
//            }
//        }
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                uiState.entries,
                { it.avid + it.pageData.cid }
            ) { item ->
                ViewListItem(item, selectedList)
            }
        }
    }
}

@Composable
private fun ViewListItem(entry: Entry, selectedList: SnapshotStateList<Entry>) {
    ListItem(
        headlineContent = {
            Text(text = entry.ownerName, maxLines = 1, fontSize = 11.sp)
        },
        overlineContent = { Text(text = entry.title, maxLines = 2) },
        leadingContent = {
            AsyncImage(
                model = entry.cover,
                contentDescription = null,
                modifier = Modifier.size(128.dp, 72.dp),
                contentScale = ContentScale.Crop
            )
        },
        trailingContent = {
            RadioButton(
                selected = isSelected(selectedList, entry),
                onClick = { changeSelected(selectedList, entry) }
            )
        }
    )
}

private fun isSelected(entries: List<Entry>, entry: Entry) = entry in entries
private fun changeSelected(entries: MutableList<Entry>, entry: Entry) {
    if (isSelected(entries, entry)) {
        entries.remove(entry)
    } else {
        entries.add(entry)
    }
}

fun getMimeType(uri: Uri, context: Context): String? {
    val mimeType: String? = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        context.contentResolver.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.lowercase(Locale.getDefault())
        )
    }
    return mimeType
}
