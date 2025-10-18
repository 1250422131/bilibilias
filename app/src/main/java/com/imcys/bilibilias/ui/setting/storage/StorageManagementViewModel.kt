package com.imcys.bilibilias.ui.setting.storage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.utils.StorageInfoData
import com.imcys.bilibilias.common.utils.StorageUtil
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StorageManagementViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    sealed interface StorageManagementUIState {
        object Loading : StorageManagementUIState
        data class Success(
            val storageInfoData: StorageInfoData,
            val hasDownloadSAFPermission: Boolean
        ) : StorageManagementUIState

        data class Error(val errorMsg: String) : StorageManagementUIState
    }

    private val _uiState =
        MutableStateFlow<StorageManagementUIState>(StorageManagementUIState.Loading)

    val uiState = _uiState.asStateFlow()

    fun loadStorageInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storageInfoData = StorageUtil.getStorageInfoData(context)
                val hasDownloadSAFPermission = StorageUtil.hasASDownloadSAFPermission(context)
                _uiState.emit(
                    StorageManagementUIState.Success(
                        storageInfoData,
                        hasDownloadSAFPermission
                    )
                )
            } catch (e: Exception) {
                _uiState.emit(StorageManagementUIState.Error(e.message ?: "未知错误"))
            }
        }
    }

    fun cleanAppCache(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value is StorageManagementUIState.Success) {
                StorageUtil.clearCache(context)
                val storageInfoData = StorageUtil.getStorageInfoData(context)
                val hasDownloadSAFPermission = StorageUtil.hasASDownloadSAFPermission(context)
                _uiState.value =
                    StorageManagementUIState.Success(storageInfoData, hasDownloadSAFPermission)
            }
        }
    }

    fun saveDownloadUri(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsRepository.saveDownloadSAFUriString(uri.toString())
            loadStorageInfo(context)
        }
    }


}