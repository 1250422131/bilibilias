package com.imcys.bilibilias.feature.settings

sealed interface UserEditEvent {
    data class onSelectedStoragePath(val path: String?) : UserEditEvent
    data class onEditNamingRule(val rule: String?) : UserEditEvent
    data class onChangeAutoMerge(val state: Boolean) : UserEditEvent
    data class onChangeAutoImport(val state: Boolean) : UserEditEvent
    data class onChangeWill(val state: Boolean) : UserEditEvent
    data class onEditCommand(val text: String?) : UserEditEvent
    data object onLogout : UserEditEvent
}
