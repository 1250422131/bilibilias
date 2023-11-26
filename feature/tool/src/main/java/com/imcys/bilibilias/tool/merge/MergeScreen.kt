package com.imcys.bilibilias.tool.merge

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.imcys.common.utils.getActivity
import com.imcys.model.download.Entry
import me.rosuh.filepicker.config.FilePickerConfig
import me.rosuh.filepicker.config.FilePickerManager
import java.util.Locale

@Composable
internal fun MergeRoute() {
    val viewModel: MergeViewModel = hiltViewModel()
    val scanResultUiState by viewModel.scanResultUiState.collectAsStateWithLifecycle()
    MergeScreen(
        scanFile = viewModel::scanFile,
        scanResultUiState = scanResultUiState,
        mixVideoAudio = viewModel::mixVideoAudio
    )
}

@Composable
internal fun MergeScreen(
    scanFile: () -> Unit,
    scanResultUiState: ScanResultUiState,
    mixVideoAudio: (List<Entry>) -> Unit
) {
    val context = LocalContext.current
    val selectedList = remember { mutableStateListOf<Entry>() }
    Scaffold(bottomBar = {
        Surface(modifier = Modifier.padding(bottom = 20.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = { pickFile(context) }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
                Button(
                    onClick = scanFile
                ) {
                    Icon(imageVector = Icons.Default.DocumentScanner, contentDescription = null)
                }
                TextButton(
                    onClick = { mixVideoAudio(selectedList) },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                ) {
                    Text(text = "合并")
                }
            }
        }
    }) { paddingValues ->
        when (scanResultUiState) {
            ScanResultUiState.Empty -> Text(text = "无")
            ScanResultUiState.Loading -> CircularProgressIndicator()
            is ScanResultUiState.Success -> LazyColumn(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(scanResultUiState.entries, { it.avid + it.pageData.cid }) { item ->
                    ListItem(
                        headlineContent = {
                            Text(text = item.ownerName, maxLines = 1, fontSize = 11.sp)
                        },
                        overlineContent = { Text(text = item.title, maxLines = 2) },
                        supportingContent = {
//                            Text(text = item.title, maxLines = 1, fontSize = 11.sp)
                        },
                        leadingContent = {
                            AsyncImage(
                                model = item.cover,
                                contentDescription = null,
                                modifier = Modifier.size(128.dp, 72.dp),
                                contentScale = ContentScale.Crop
                            )
                        },
                        trailingContent = {
                            RadioButton(
                                selected = isSelected(selectedList, item),
                                onClick = { changeSelected(selectedList, item) }
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun isSelected(entries: List<Entry>, entry: Entry) = entry in entries
private fun changeSelected(entries: MutableList<Entry>, entry: Entry) {
    if (isSelected(entries, entry)) {
        entries.remove(entry)
    } else {
        entries.add(entry)
    }
}
//  LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
//                val data = FilePickerManager.obtainData(true)
//                if (data.size % 2 != 0) {
//                    Toaster.showShort("请选择视频和音频文件！")
//                    return@LifecycleEventEffect
//                }
//                data.filter { isVideo(it) }.forEach { mixVideoAudioList.add(it) }
//                data.filter { isAudio(it) }.forEach { mixVideoAudioList.add(it) }
//            }
//
//            mixVideoAudioList.forEach {
//                if (isVideo(it)) {
//                    Card(modifier = Modifier.fillMaxWidth()) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(4.dp)
//                        ) {
//                            Row {
//                                Text(text = "视频路径: ")
//                                Text(
//                                    text = it.drop(BILI_FULL_PATH.length),
//                                    modifier = Modifier.fillMaxWidth()
//                                )
//                            }
//                            Text(text = getVideoCodecInfo(it)?.name.toString())
//                            Text(text = "视频比特率: " + getVideoBitRate(it).toString())
//                            Text(text = "FPS: " + getVideoFPS(it).toString())
//                        }
//                    }
//                }
//                if (isAudio(it)) {
//                    Card(modifier = Modifier.fillMaxWidth()) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(4.dp)
//                        ) {
//                            Row {
//                                Text(text = "音频路径: ")
//                                Text(
//                                    text = it.drop(BILI_FULL_PATH.length),
//                                    modifier = Modifier.fillMaxWidth()
//                                )
//                            }
//                            Text(text = getAudioCodecInfo(it)?.name.toString())
//                            Text(text = "音频比特率: " + getAudioBitRate(it))
//                            Text(text = "音频采样率: " + getAudioSampleRate(it))
//                        }
//                    }
//                }
//            }

const val BASE_PATH = "/storage/emulated/0/Android/data/"
const val BILI_PATH = "tv.danmaku.bili/download"
const val BILI_FULL_PATH = BASE_PATH + BILI_PATH
fun pickFile(context: Context) {
    FilePickerManager
        .from(context.getActivity())
        .setCustomRootPath(BILI_FULL_PATH)
        .storageType("m4s", FilePickerConfig.STORAGE_CUSTOM_ROOT_PATH)
        .maxSelectable(2)
//                    .filter(
//                        object : AbstractFileFilter() {
//                        override fun doFilter(listData: ArrayList<FileItemBeanImpl>): ArrayList<FileItemBeanImpl> {
//                            return ArrayList(listData.filter { it.fileType })
//                        }
//                    })
        .forResult(FilePickerManager.REQUEST_CODE)
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
