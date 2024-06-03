package com.imcys.bilibilias.feature.settings

sealed interface UserEditEvent {
    data class onChangeStoragePath(val path: String?) : UserEditEvent
    data class onChangeFileNamingRule(val rule: String?) : UserEditEvent
    data class onChangeAutoMerge(val state: Boolean) : UserEditEvent
    data class onChangeAutoImport(val state: Boolean) : UserEditEvent
    data class onChangeWill(val state: Boolean) : UserEditEvent
    data class onChangeCommand(val state: Boolean) : UserEditEvent
}
