package com.imcys.bilibilias.core.download

import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid

data class DownloadRequest(
    val aid: Aid,
    val bvid: Bvid,
    val cid: Cid,
    val codecid: Int,
    val taskType: TaskType,
    val quality: Int,
)
