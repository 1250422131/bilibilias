package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import kotlin.uuid.ExperimentalUuidApi

class AsPreferencesDataSource(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
    @OptIn(ExperimentalUuidApi::class)
    suspend fun setSelfInfo(info: SelfInfo) {
        userPreferences.updateData {
            it.copy(selfInfo = info)
        }
    }
}