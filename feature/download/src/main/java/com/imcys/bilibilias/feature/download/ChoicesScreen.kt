package com.imcys.bilibilias.feature.download

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.ViewInfo
import io.github.aakira.napier.Napier

internal class ChoicesScreen(
    private val aid: Aid,
    private val bvid: Bvid,
    private val cid: Cid,
    private val fileType: FileType,
    private val navigationToPlayer: (vUri: Uri, aUri: Uri) -> Unit
) : Screen {
    @Composable
    override fun Content() {
//        val viewModel: DownloadViewModel = getViewModel()
        val list = remember { mutableStateListOf<DownloadTaskEntity>() }
        LaunchedEffect(aid, cid) {
            list.clear()
//            list.addAll(viewModel.getPlayerInfo(aid, bvid, cid))
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsButton(
                onClick = {
//                    viewModel.onDelete(ViewInfo(aid, bvid, cid, ""), fileType)
//                    sheetNavigator.hide()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "删除")
            }
            AsButton(
                onClick = {
                    val v = list.find { it.fileType == FileType.VIDEO }
                    val a = list.find { it.fileType == FileType.AUDIO }

                    v?.let { v1 ->
                        a?.let { navigationToPlayer(v1.uri, it.uri) }
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "播放")
            }
        }
    }
}
