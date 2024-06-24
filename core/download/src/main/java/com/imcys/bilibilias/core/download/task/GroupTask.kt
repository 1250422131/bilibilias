package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.database.model.Task

data class GroupTask(val video: DownloadTaskEntity, val audio: DownloadTaskEntity)
