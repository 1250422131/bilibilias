package com.imcys.datastore.mmkv

object SettingsRepository : MMKVOwner(mmapID = "Settings") {

    var autoImportToBilibili by mmkvBool()
    var autoMergeVideoAndAudio by mmkvBool()

    const val DEFAULT_SAVE_FILE_PATH = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download"
    var saveFilePath by mmkvString()

    // 百度统计
    var baiduStatistics by mmkvBool(true)

    // 微软统计
    var microsoftStatistics by mmkvBool(true)

    // 谷歌广告
    var googleAD by mmkvBool(true)

    const val DefaultVideoNameRule = "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
    var videoNameRule by mmkvString()
        private set

    fun saveVideoNamingRule(rules: String) {
        videoNameRule = rules
    }

    fun resetVideoNamingRule() {
        videoNameRule = DefaultVideoNameRule
    }
}
