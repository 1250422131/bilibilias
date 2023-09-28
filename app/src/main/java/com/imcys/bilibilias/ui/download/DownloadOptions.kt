package com.imcys.bilibilias.ui.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.common.base.model.video.VideoDetails

enum class DownloadToolType {
    // 内建
    BUILTIN,
    IDM,
    ADM
}

class DownloadOptionsStateHolders {
    var videoClarity by mutableStateOf("")
    var cachedSubset = mutableStateListOf<VideoDetails.Page>()
    var cacheType by mutableStateOf("")
    var cachedAudioQuality by mutableIntStateOf(-1)
}

@Composable
fun rememberDownloadOptions(): DownloadOptionsStateHolders {
    return remember {
        DownloadOptionsStateHolders()
    }
}
