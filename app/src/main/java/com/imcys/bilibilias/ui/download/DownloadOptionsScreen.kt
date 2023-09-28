package com.imcys.bilibilias.ui.download

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.CenterRow
import com.imcys.bilibilias.common.base.components.LeadingTrailingIconRow
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.ui.player.PlayerState

@Composable
fun DownloadOptionsScreen(
    state: PlayerState,
    onNavigateToVideoClarity: () -> Unit,
    onNavigateToCachedSubset: () -> Unit,
    onNavigateToCacheType: () -> Unit,
    onNavigateToCachedAudioQuality: () -> Unit,
    onBack: () -> Unit,
    downloadOptions: DownloadOptionsStateHolders,
    getVideoPlayList: (String) -> Unit,
    downloadVideo: () -> Unit,
) {
    LaunchedEffect(Unit) {
        getVideoPlayList(state.videoDetails.bvid)
    }
    Column(Modifier.padding(horizontal = 20.dp)) {
        CenterRow(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text(
                stringResource(R.string.app_dialog_dl_video_bar_title),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        var showVideoClarity by rememberSaveable { mutableStateOf(false) }
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_video_definition,
            R.string.app_dialog_dl_video_definition,
            downloadOptions.videoClarity,
            Modifier.clickable {
                showVideoClarity = !showVideoClarity
            }
        )
        if (showVideoClarity) {
            LazyRow(contentPadding = PaddingValues(8.dp)) {
                items(state.videoPlayDetails.acceptDescription) { item ->
                    TextButton(onClick = { downloadOptions.videoClarity = item }) {
                        Text(
                            item,
                            color = if (item == downloadOptions.videoClarity) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Unspecified
                            }
                        )
                    }
                }
            }
        }
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_video_diversity,
            R.string.app_dialog_dl_video_episode,
            "P1",
            Modifier.clickable { onNavigateToCachedSubset() }
        )
        var showVideoType by rememberSaveable { mutableStateOf(false) }
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_video_download,
            R.string.app_dialog_dl_video_video_type,
            downloadOptions.cacheType,
            Modifier.clickable { showVideoType = !showVideoType }
        )
        val dash = remember { "Dash" }
        val mp4 = remember { "Mp4" }
        if (showVideoType) {
            CenterRow {
                TextButton(onClick = { downloadOptions.cacheType = dash }) {
                    Text(
                        dash,
                        color = if (downloadOptions.cacheType == dash) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Unspecified
                        }
                    )
                }
                TextButton(onClick = { downloadOptions.cacheType = mp4 }) {
                    Text(
                        mp4,
                        color = if (downloadOptions.cacheType == mp4) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Unspecified
                        }
                    )
                }
            }
        }
        var showAudioQuality by remember { mutableStateOf(false) }
        DownloadBottomSheetLeadingTrailingIcon(
            R.drawable.ic_as_audio_download_monitor,
            R.string.app_dialog_dl_video_audio_quality,
            "192K",
            Modifier.clickable {
                showAudioQuality = !showAudioQuality
            }
        )
        if (showAudioQuality) {
            LazyRow(contentPadding = PaddingValues(8.dp)) {
                items(state.videoPlayDetails.acceptQuality) { item ->
                    TextButton(onClick = { downloadOptions.cachedAudioQuality = item }) {
                        Text(
                            AsVideoUtils.getQualityName(item),
                            color = if (item == downloadOptions.cachedAudioQuality) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Unspecified
                            }
                        )
                    }
                }
            }
        }
        DownloadToolTypeRadioGroup()
        DownloadVideoOrAudioRadioGroup()
        val viewModel : DownloadViewModel = hiltViewModel()
        Button(
            onClick = {

                viewModel.downloadVideo(state.videoDetails.bvid, state.videoDetails.cid,
                    state.videoPlayDetails.durl.first().url)
                state.videoDetails.pages.forEach {
                    downloadOptions.cachedSubset.add(it)
                }
                downloadVideo()
            },
            Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(stringResource(R.string.app_dialog_dl_video_button))
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun DownloadVideoOrAudioRadioGroup() {
    val (type, setType) = remember { mutableStateOf(DownloadToolType.BUILTIN) }
    Row {
        RadioGroup(
            DownloadToolType.values(),
            type,
            setType,
            listOf(
                stringResource(R.string.app_dialog_dl_radio_video_and_audio),
                stringResource(R.string.app_dialog_dl_radio_only_audio),
                stringResource(R.string.app_dialog_dl_radio_only_video)
            )
        )
    }
}

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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        trailingText = {
            Text(
                trailingText,
                Modifier.padding(horizontal = 10.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
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
