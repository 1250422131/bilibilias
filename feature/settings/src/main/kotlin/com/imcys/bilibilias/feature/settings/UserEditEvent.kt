package com.imcys.bilibilias.feature.settings

sealed interface UserEditEvent {
    data class SelectedStoragePath(val path: String?) : UserEditEvent
    data class EditNamingRule(val rule: String?) : UserEditEvent
    data class ChangeAutoMerge(val state: Boolean) : UserEditEvent
    data class ChangeAutoImport(val state: Boolean) : UserEditEvent
    data class ChangeWill(val state: Boolean) : UserEditEvent
    data class EditCommand(val text: String?) : UserEditEvent
    data object Logout : UserEditEvent
    sealed interface ShareLog : UserEditEvent {
        data object NewLog : ShareLog
        data object OldLog : ShareLog
    }
}
