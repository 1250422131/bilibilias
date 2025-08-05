package com.imcys.bilibilias.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.model.BILISpaceArchiveModel
import com.imcys.bilibilias.data.model.BILIUserStatModel
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    data class UIState(
        val biliUsersEntity: BILIUsersEntity? = null
    )

    private val _uiState = MutableStateFlow<UIState>(UIState())
    val uiState = _uiState.asStateFlow()

    private val _userPageInfoState =
        MutableStateFlow<NetWorkResult<BILIUserSpaceAccInfo?>>(emptyNetWorkResult())
    val userPageInfoState = _userPageInfoState.asStateFlow()

    private val _userStatInfoState =
        MutableStateFlow(
            BILIUserStatModel(
                emptyNetWorkResult(),
                emptyNetWorkResult()
            )
        )
    val userStatInfoState = _userStatInfoState.asStateFlow()


    private val _spaceArchiveInfoState =
        MutableStateFlow<NetWorkResult<BILISpaceArchiveModel?>>(emptyNetWorkResult())

    val spaceArchiveInfoState = _spaceArchiveInfoState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    biliUsersEntity = userInfoRepository.getBILIUserByUid()
                )
            )
        }
    }

    fun getUserPageIno(mid: Long) {
        if (mid == 0L) return
        // 当请求的mid和当前加载的mid一致，并且已经成功获取，则不再请求
        if (mid == _userPageInfoState.value.data?.mid) {
            return
        }
        viewModelScope.launch {
            userInfoRepository.getUserPageInfo(mid).collect {
                _userPageInfoState.emit(it)
            }
        }
        viewModelScope.launch {
            userInfoRepository.getUserStatInfo(mid).collect {
                _userStatInfoState.emit(it)
            }
        }
        viewModelScope.launch {
            userInfoRepository.getSpaceArchiveInfo(mid).collect {
                _spaceArchiveInfoState.emit(it)
            }
        }
    }

}