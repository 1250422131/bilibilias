package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvBool
import com.imcys.bilibilias.common.base.extend.mmkvString

object SettingsRepository : MMKVOwner(mmapID = "Settings") {

    var autoImportBilibili by mmkvBool()

    const val DEFAULT_SAVE_FILE_PATH = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download"
    var saveFilePath by mmkvString()

    // 百度统计
    var baiduStatistics by mmkvBool(true)

    // 微软统计
    var microsoftStatistics by mmkvBool(true)

    // 谷歌广告
    var googleAD by mmkvBool(true)

    const val DefaultVideoNamingRule = "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
    var videoNamingRule by mmkvString()
        private set

    fun saveVideoNamingRule(rules: String) {
        videoNamingRule = rules
    }

    fun resetVideoNamingRule() {
        videoNamingRule = DefaultVideoNamingRule
    }
}
