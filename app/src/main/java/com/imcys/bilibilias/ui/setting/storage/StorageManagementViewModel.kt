package com.imcys.bilibilias.ui.setting.storage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.utils.StorageInfoData
import com.imcys.bilibilias.common.utils.StorageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StorageManagementViewModel : ViewModel() {

    sealed interface StorageManagementUIState {
        object Loading : StorageManagementUIState
        data class Success(val storageInfoData: StorageInfoData) : StorageManagementUIState
        data class Error(val errorMsg: String) : StorageManagementUIState
    }

    private val _uiState =
        MutableStateFlow<StorageManagementUIState>(StorageManagementUIState.Loading)

    val uiState = _uiState.asStateFlow()

    fun loadStorageInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storageInfoData = StorageUtil.getStorageInfoData(context)
                _uiState.emit(StorageManagementUIState.Success(storageInfoData))
            } catch (e: Exception) {
                _uiState.emit(StorageManagementUIState.Error(e.message ?: "未知错误"))
            }
        }
    }


}