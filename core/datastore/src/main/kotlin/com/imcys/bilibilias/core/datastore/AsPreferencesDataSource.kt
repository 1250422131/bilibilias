package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import com.imcys.bilibilias.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AsPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map {
        UserData(
            storageFolder = it.storageFolder,
            fileNamesConvention = it.fileNamesConvention,
            ffmpegCommand = it.ffmpegCommand,
        )
    }

    suspend fun setStorageFolder(path: String?) {
        userPreferences.updateData { it.copy(storageFolder = path) }
    }

    suspend fun setFileNamesConvention(rule: String?) {
        userPreferences.updateData { it.copy(fileNamesConvention = rule) }
    }

    suspend fun setFfmpegCommand(command: String?) {
        userPreferences.updateData { it.copy(ffmpegCommand = command) }
    }
}
