package com.imcys.player.download

import androidx.compose.runtime.Composable
import com.imcys.model.video.PageData
import com.imcys.network.download.DownloadListHolders
import com.imcys.network.download.DownloadToolType
import com.imcys.player.PlayerState

@Composable
fun DownloadOptionsScreen(
    state: PlayerState,
    onBack: () -> Unit,
    downloadOptions: DownloadListHolders,
    downloadVideo: (com.imcys.model.VideoDetails, DownloadListHolders) -> Unit,
    setDownloadTool: (DownloadToolType) -> Unit,
    downloadListState: DownloadListState,
    setRequireDownloadFileType: (DownloadFileType) -> Unit,
    setAcceptDescription: (Int) -> Unit,
    setPages: (PageData) -> Unit,
) {}
//     Box(Modifier.statusBarsPadding()) {
//         Column(Modifier.padding(horizontal = 8.dp)) {
//             // todo 此处标题用词不太对
//             Text(
//                 stringResource(R.string.app_dialog_dl_video_bar_title),
//                 Modifier.padding(8.dp),
//                 color = MaterialTheme.colorScheme.primary,
//                 fontWeight = FontWeight.Bold
//             )
//             Divider()
//
//             // <editor-fold desc="视频清晰度">
//             var showVideoClarity by rememberSaveable { mutableStateOf(false) }
//             DownloadBottomSheetLeadingTrailingIcon(
//                 R.drawable.ic_as_video_definition,
//                 R.string.app_dialog_dl_video_definition,
//                 downloadListState.selectedDescription,
//                 Modifier.clickable {
//                     showVideoClarity = !showVideoClarity
//                 }
//             )
//             if (showVideoClarity) {
//                 LazyRow(contentPadding = PaddingValues(4.dp)) {
//                     itemsIndexed(downloadListState.availableAcceptDescription) { index, item ->
//                         TextButton(onClick = { setAcceptDescription(index) }) {
//                             Text(item)
//                         }
//                     }
//                 }
//             }
//             // </editor-fold>
//
//             DownloadToolTypeRadioGroup(setDownloadTool, downloadListState.downloadTool)
//             DownloadVideoOrAudioRadioGroup(setRequireDownloadFileType, downloadListState.requireDownloadFileType)
//             // <editor-fold desc="选择文件下载">
//             LazyColumn(Modifier.padding(bottom = 70.dp)) {
//                 items(downloadListState.availablePages, key = { it.cid }) {
//                     TextButton(
//                         onClick = { setPages(it) },
//                         border = BorderStroke(
//                             2.dp,
//                             if (it in downloadListState.selectedPages) MaterialTheme.colorScheme.primary else Color.Unspecified
//                         )
//                     ) {
//                         SingleLineText(it.part, Modifier.fillMaxWidth())
//                     }
//                 }
//             }
//             // </editor-fold>
//         }
//         Surface(
//             Modifier
//                 .align(Alignment.BottomCenter),
//             color = Color.Unspecified
//         ) {
//             Button(
//                 onClick = {
//                     downloadVideo(
//                         state.videoDetails,
//                         downloadOptions,
//                     )
//                 },
//                 Modifier
//                     .fillMaxWidth()
//                     .height(60.dp)
//             ) {
//                 Text(stringResource(R.string.app_dialog_dl_video_button))
//             }
//         }
//     }
// }

// @Composable
// fun DownloadVideoOrAudioRadioGroup(
//     setDownloadFileType: (DownloadFileType) -> Unit,
//     requireDownloadFileType: DownloadFileType
// ) {
//     Row {
//         RadioGroup(
//             DownloadFileType.values(),
//             requireDownloadFileType,
//             setDownloadFileType,
//             listOf(
//                 stringResource(R.string.app_dialog_dl_radio_video_and_audio),
//                 stringResource(R.string.app_dialog_dl_radio_only_audio),
//             )
//         )
//     }
// }

/**
 * 下载工具
 */
// @Composable
// fun DownloadToolTypeRadioGroup(setDownloadTool: (DownloadToolType) -> Unit, downloadTool: DownloadToolType) {
//     Row {
//         RadioGroup(
//             DownloadToolType.values(),
//             downloadTool,
//             setDownloadTool,
//             listOf(
//                 stringResource(R.string.app_dialog_dl_video_radio_built_download),
//                 stringResource(R.string.app_dialog_dl_video_idm_dl),
//                 stringResource(R.string.app_dialog_dl_video_adm_dl)
//             )
//         )
//     }
// }

// @Composable
// private fun DownloadBottomSheetLeadingTrailingIcon(
//     @DrawableRes leadingIcon: Int,
//     @StringRes leadingText: Int,
//     trailingText: String,
//     modifier: Modifier = Modifier,
//     imageVector: ImageVector = Icons.Default.ArrowForwardIos
// ) {
//     LeadingTrailingIconRow(
//         leadingIcon = {
//             Image(
//                 painterResource(leadingIcon),
//                 contentDescription = null,
//                 modifier = Modifier.size(20.dp),
//                 contentScale = ContentScale.Crop,
//                 colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
//             )
//         },
//         leadingText = {
//             Text(
//                 stringResource(leadingText),
//                 Modifier.padding(horizontal = 10.dp),
//             )
//         },
//         trailingText = {
//             Text(
//                 trailingText,
//                 Modifier.padding(horizontal = 10.dp),
//             )
//         },
//         trailingIcon = { Image(imageVector = imageVector, contentDescription = null) },
//         modifier = modifier.padding(horizontal = 10.dp, vertical = 4.dp)
//     )
// }

// @Composable
// fun <T> RadioGroup(items: Array<T>, selected: T, onClick: (T) -> Unit, text: List<String>) {
//     Row(verticalAlignment = Alignment.CenterVertically) {
//         items.forEachIndexed { index, item ->
//             RadioButton(
//                 selected = item == selected,
//                 onClick = { onClick(item) },
//                 colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
//             )
//             Text(text[index])
//         }
//     }
// }
//
// @Preview
// @Composable
// private fun PreviewLeadingTrailingIconRow() {
//     Column {
//         DownloadBottomSheetLeadingTrailingIcon(
//             R.drawable.ic_as_video_definition,
//             R.string.app_dialog_dl_video_definition,
//             "1080P"
//         )
//         DownloadBottomSheetLeadingTrailingIcon(
//             R.drawable.ic_as_video_diversity,
//             R.string.app_dialog_dl_video_episode,
//             "P1"
//         )
//         DownloadBottomSheetLeadingTrailingIcon(
//             R.drawable.ic_as_video_download,
//             R.string.app_dialog_dl_video_video_type,
//             "DASH"
//         )
//         DownloadBottomSheetLeadingTrailingIcon(
//             R.drawable.ic_as_audio_download_monitor,
//             R.string.app_dialog_dl_video_audio_quality,
//             "192K"
//         )
//     }
// }

// @Preview
// @Composable
// fun PreviewRadioGroup() {
//     RadioGroup(arrayOf("h"), "h", {}, emptyList())
// }