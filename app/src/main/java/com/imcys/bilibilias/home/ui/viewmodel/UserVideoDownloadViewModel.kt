package com.imcys.bilibilias.home.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class UserVideoDownloadViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var networkService: NetworkService

    private var _loadInfo = MutableLiveData(LoadInfo())
    val loadInfo: LiveData<LoadInfo>
        get() = _loadInfo

    private var _userVideo =
        MutableLiveData<MutableList<UserWorksBean.DataBean.ListBean.VlistBean>>(mutableListOf())

    val userVideo: LiveData<MutableList<UserWorksBean.DataBean.ListBean.VlistBean>>
        get() = _userVideo

    /**
     * 批量获取用户数据->分页请求
     */
    fun loadUserAllVideo(mid: String) {
        viewModelScope.launchIO {
            var userWorkerBean = networkService.getUserWorkData(mid.toLong(), 1)

            if (userWorkerBean.code == 0) {
                val totalPage =
                    ceil((userWorkerBean.data.page.count.toDouble() / userWorkerBean.data.page.ps))
                var thisPageIndex = 1

                _loadInfo.postValue(
                    _loadInfo.value?.apply {
                        progression = ((thisPageIndex.toDouble() / totalPage) * 100).toInt()
                        tip = "正在获取第1页数据"
                        loadState = true
                    }
                )

                _userVideo.postValue((_userVideo.value?.plus(userWorkerBean.data.list.vlist))?.toMutableList())

                for (i in 2..totalPage.toInt()) {
                    _loadInfo.postValue(_loadInfo.value?.apply { tip = "正在获取第${i}页数据" })
                    userWorkerBean = networkService.getUserWorkData(mid.toLong(), ++thisPageIndex)

                    if (userWorkerBean.code == 0) {
                        _loadInfo.postValue(
                            _loadInfo.value?.apply {
                                progression = ((thisPageIndex.toDouble() / totalPage) * 100).toInt()
                            }
                        )
                        _userVideo.postValue((_userVideo.value?.plus(userWorkerBean.data.list.vlist))?.toMutableList())
                    }
                }

                _loadInfo.postValue(
                    _loadInfo.value?.apply {
                        loadState = false
                    }
                )
            }
        }
    }

    data class LoadInfo(
        var tip: String = "",
        var progression: Int = 0,
        var loadState: Boolean = false,
    )
}
