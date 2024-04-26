package com.imcys.bilibilias.core.download

import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.AudioTask
import com.imcys.bilibilias.core.download.task.VideoTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.kotlin.listener.createListener1
import javax.inject.Inject

class DownloadTaskExecute @Inject constructor() {

}
internal typealias TaskEnd = (viewinfo: ViewInfo, type: FileType) -> Unit
