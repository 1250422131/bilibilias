package com.imcys.bilibilias.core.common.download

object DefaultConfig {
    const val DEFAULT_NAMING_RULE = "{TITLE}/{P_TITLE}_{CID}"
    const val DEFAULT_STORE_PATH = "bilibilias/download"
    const val DEFAULT_COMMAND = "ffmpeg -y -i {VIDEO_PATH} -i {AUDIO_PATH} -c copy {VIDEO_MERGE_PATH}"
}
