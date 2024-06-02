package com.imcys.bilibilias.feature.settings

sealed interface UserEditEvent {
    data class onChangeStoragePath(val path: String?) : UserEditEvent
    data class onChangeFileNamingRule(val rule: String?) : UserEditEvent
    data object onChangeAutoMerge : UserEditEvent
    data object onChangeAutoImport : UserEditEvent
    data object onChangeWill : UserEditEvent
    data object onChangeCommand : UserEditEvent
}
