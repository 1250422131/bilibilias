package com.imcys.bilibilias.ui.download

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.LeadingTrailingIconRow
import com.imcys.bilibilias.common.base.components.SingleLineText
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.common.data.download.entity.DownloadFileType
import com.imcys.bilibilias.ui.player.PlayerState

@Composable
fun DownloadOptionsScreen(
    state: PlayerState,
    onBack: () -> Unit,
    downloadOptions: DownloadOptionsStateHolders,
    downloadVideo: (String, DownloadOptionsStateHolders) -> Unit,
) {
    Box(Modifier.statusBarsPadding()) {
        Column(Modifier.padding(horizontal = 8.dp)) {
            // todo 此处标题用词不太对
            Text(
                stringResource(R.string.app_dialog_dl_video_bar_title),
                Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Divider()

            // <editor-fold desc="视频清晰度">
            var showVideoClarity by rememberSaveable { mutableStateOf(false) }
            DownloadBottomSheetLeadingTrailingIcon(
                R.drawable.ic_as_video_definition,
                R.string.app_dialog_dl_video_definition,
                downloadOptions.videoFormatDescription,
                Modifier.clickable {
                    showVideoClarity = !showVideoClarity
                }
            )
            if (showVideoClarity) {
                LazyRow(contentPadding = PaddingValues(4.dp)) {
                    itemsIndexed(state.dashVideo.acceptDescription) { index, item ->
                        TextButton(onClick = {
                            downloadOptions.videoFormatDescription = item
                            downloadOptions.videoQuality = state.dashVideo.acceptQuality[index]
                        }) {
                            Text(item)
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold desc="音质选择">
            var showAudioQuality by remember { mutableStateOf(false) }
            DownloadBottomSheetLeadingTrailingIcon(
                R.drawable.ic_as_audio_download_monitor,
                R.string.app_dialog_dl_video_audio_quality,
                AsVideoUtils.getQualityName(downloadOptions.audioFormatDescription),
                Modifier.clickable {
                    showAudioQuality = !showAudioQuality
                }
            )
            if (showAudioQuality) {
                LazyRow(contentPadding = PaddingValues(8.dp)) {
                    items(state.dashVideo.dash.audio) { item ->
                        TextButton(onClick = {
                            downloadOptions.audioFormatDescription = item.id; downloadOptions.audioQuality = item.id
                        }) {
                            Text(AsVideoUtils.getQualityName(item.id))
                        }
                    }
                }
            }
            // </editor-fold>

            DownloadToolTypeRadioGroup()
            DownloadVideoOrAudioRadioGroup(downloadOptions)
            // <editor-fold desc="选择文件下载">
            val subSets = remember { mutableStateListOf(state.videoDetails.pages.first()) }
            LazyColumn(Modifier.padding(bottom = 70.dp)) {
                items(state.videoDetails.pages, key = { it.cid }) {
                    TextButton(
                        onClick = {
                            if (it in subSets) {
                                subSets.remove(it)
                            } else {
                                subSets.add(it)
                            }
                        },
                        border = BorderStroke(
                            2.dp,
                            if (it in subSets) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    ) {
                        SingleLineText(it.part, Modifier.fillMaxWidth())
                    }
                }
            }
            // </editor-fold>
        }
        Surface(
            Modifier
                .align(Alignment.BottomCenter),
            color = Color.Unspecified
        ) {
            Button(
                onClick = {
                    downloadVideo(
                        state.videoDetails.bvid,
                        downloadOptions,
                    )
                },
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(stringResource(R.string.app_dialog_dl_video_button))
            }
        }
    }
}

@Composable
fun DownloadVideoOrAudioRadioGroup(downloadOptions: DownloadOptionsStateHolders) {
    Row {
        RadioGroup(
            DownloadFileType.values(),
            downloadOptions.requireDownloadFileType,
            {
                downloadOptions.requireDownloadFileType = it
            },
            listOf(
                stringResource(R.string.app_dialog_dl_radio_video_and_audio),
                stringResource(R.string.app_dialog_dl_radio_only_audio),
            )
        )
    }
}

/**
 * 下载工具
 */
@Composable
fun DownloadToolTypeRadioGroup() {
    val (type, setType) = remember { mutableStateOf(DownloadToolType.BUILTIN) }
    Row {
        RadioGroup(
            DownloadToolType.values(),
            type,
            setType,
            listOf(
                stringResource(R.string.app_dialog_dl_video_radio_built_download),
                stringResource(R.string.app_dialog_dl_video_idm_dl),
                stringResource(R.string.app_dialog_dl_video_adm_dl)
            )
        )
    }
}

@Composable
private fun DownloadBottomSheetLeadingTrailingIcon(
    @DrawableRes leadingIcon: Int,
    @StringRes leadingText: Int,
    trailingText: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.ArrowForwardIos
) {
    LeadingTrailingIconRow(
        leadingIcon = {
            Image(
                painterResource(leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        },
        leadingText = {
            Text(
                stringResource(leadingText),
                Modifier.padding(horizontal = 10.dp),
            )
        },
        trailingText = {
            Text(
                trailingText,
                Modifier.padding(horizontal = 10.dp),
            )
        },
        trailingIcon = { Image(imageVector = imageVector, contentDescription = null) },
        modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    )
}

@Composable
fun <T> RadioGroup(items: Array<T>, selected: T, onClick: (T) -> Unit, text: List<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        items.forEachIndexed { index, t ->
            RadioButton(
                selected = t == selected,
                onClick = { onClick(t) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
            Text(text[index])
        }
    }
}

@Preview
@Composable
private fun PreviewLeadingTrailingIconRow() {
    Column {
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_video_definition,
            R.string.app_dialog_dl_video_definition,
            "1080P"
        )
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_video_diversity,
            R.string.app_dialog_dl_video_episode,
            "P1"
        )
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_video_download,
            R.string.app_dialog_dl_video_video_type,
            "DASH"
        )
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_audio_download_monitor,
            R.string.app_dialog_dl_video_audio_quality,
            "192K"
        )
    }
}

@Preview
@Composable
fun PreviewRadioGroup() {
    RadioGroup(arrayOf("h"), "h", {}, emptyList())
}
