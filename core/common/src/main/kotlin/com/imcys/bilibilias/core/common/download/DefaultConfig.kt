package com.imcys.bilibilias.core.common.download

object DefaultConfig {
    const val defaultNamingRule = "{TITLE}/{P_TITLE}_{CID}"
    const val defaultStorePath = "bilibilias/download"
    const val defaultCommand = "ffmpeg -y -i {VIDEO_PATH} -i {AUDIO_PATH} -c copy {VIDEO_MERGE_PATH}"
}
