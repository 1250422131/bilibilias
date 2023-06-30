package com.imcys.bilibilias.home.ui.model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.home.ui.model.UpStatBeam
import com.imcys.bilibilias.home.ui.model.UserBaseBean
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//意图
sealed class UserIntent {
    object GetUserBaseBean : UserIntent()
    object GetUserCardData : UserIntent()
    data class GetUserWorksBean(var qn: Int, var ps: Int = 20) : UserIntent()
}

class UserViewModel : ViewModel() {


    data class UserViewState(
        var userBaseBean: UserBaseBean = UserBaseBean(),
        var userCardBean: UserCardBean? = UserCardBean(),
        var upStatBeam: UpStatBeam? = UpStatBeam(),
        var userWorksBean: UserWorksBean? = UserWorksBean(),
    )


    val userChannel = Channel<UserIntent>(Channel.UNLIMITED)
    var viewStates by mutableStateOf(UserViewState())
        private set


    init {
        //管道数据获取
        handleIntent()
    }

    private fun handleIntent() {

        viewModelScope.launch {
            userChannel.consumeAsFlow().collect {
                when (it) {
                    is UserIntent.GetUserBaseBean -> getUserBaseData()
                    is UserIntent.GetUserCardData -> getUserCardData()
                    is UserIntent.GetUserWorksBean -> getUserWorksBean(it.qn, it.ps)
                }
            }
        }
    }


    private fun getUserBaseData() {
        viewModelScope.launch {
            latestUserBaseData.flowOn(Dispatchers.Default)
                .catch {
                }.collect {
                    viewStates = viewStates.copy(
                        userBaseBean = it
                    )
                }

        }
    }


    private fun getUserWorksBean(qn: Int, ps: Int) {
        viewModelScope.launch {
            val userWorksBean = withContext(viewModelScope.coroutineContext) {
                return@withContext HttpUtils.asyncGet(
                    "${BilibiliApi.userWorksPath}?mid=1&qn=$qn&ps=$ps",
                    UserWorksBean::class.java
                )
            }
            viewStates = viewStates.copy(
                userWorksBean = userWorksBean
            )
        }
    }


    private fun getUserCardData() {
        viewModelScope.launch {
            latestUserCardData.flowOn(Dispatchers.Default)
                .catch {
                }.collect {
                    viewStates = viewStates.copy(
                        userCardBean = it
                    )
                }

        }
        getUpStatData()
    }

    private fun getUpStatData() {
        viewModelScope.launch {
            latestUpStatBeamData.flowOn(Dispatchers.Default)
                .catch {
                }.collect {
                    viewStates = viewStates.copy(
                        upStatBeam = it
                    )
                }

        }
    }


    private val latestUserBaseData: Flow<UserBaseBean> = flow {
        val userBaseBean = withContext(Dispatchers.IO) {
            HttpUtils.addHeader("cookie", "")
                .asyncGet(
                    "${BilibiliApi.userBaseDataPath}?mid=1",
                    UserBaseBean::class.java
                )
        }
        //返回拉取结果
        emit(userBaseBean)
    }

    private val latestUserCardData: Flow<UserCardBean> = flow {
        val userCardBean = withContext(Dispatchers.IO) {
            HttpUtils.addHeader("cookie", "等待填充")
                .asyncGet(
                    "${BilibiliApi.getUserCardPath}?mid=1",
                    UserCardBean::class.java
                )
        }
        //返回拉取结果
        emit(userCardBean)
    }

    private val latestUpStatBeamData: Flow<UpStatBeam> = flow {
        val upStatBeam = withContext(Dispatchers.IO) {
            HttpUtils.addHeader("cookie", "等待填充")
                .asyncGet(
                    "${BilibiliApi.userUpStat}?mid=1",
                    UpStatBeam::class.java
                )
        }
        //返回拉取结果
        emit(upStatBeam)
    }


}

