package com.imcys.bilibilias.ui.user

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.imcys.bilibilias.base.utils.WbiUtils
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.config.UserInfoRepository
import com.imcys.bilibilias.common.base.constant.MID
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.home.ui.model.UpStatBean
import com.imcys.bilibilias.home.ui.model.UserBaseBean
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserNavDataBean
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val http: HttpClient) : BaseViewModel() {
    private val _userDataState = MutableStateFlow(UserState())
    val userDataState = _userDataState.asStateFlow()

    private var mid = 0L

    init {
        getUserInformation()
    }

    private fun getUserInformation() {
        launchIO {
            val mid = getMyUserData()

            val params = getWbiKey(mid)

            launch { getUserData(params) }
            launch { getUserCardBean(params) }
            launch { getUpStat() }
        }
    }

    /**
     * 获取用户状态信息
     */
    private suspend fun getUpStat() {
        val bean = http.get(BilibiliApi.userUpStat) {
            parameter(MID, mid)
        }.body<UpStatBean>()
        _userDataState.update {
            it.copy(likes = bean.likes, archive = bean.archive.view)
        }
    }

    /**
     * 获取用户卡片信息
     */
    private suspend fun getUserCardBean(params: List<Pair<String, String>>) {
        // UpStatBean.data.likes
        // UpStatBean.data.archive
        val bean = http.get(BilibiliApi.getUserCardPath) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }.body<UserCardBean>()
        _userDataState.update {
            it.copy(fans = bean.card.fans, friend = bean.card.friend)
        }
    }

    private suspend fun getMyUserData(): Long {
        if (UserInfoRepository.mid != 0L && UserInfoRepository.mid == mid) return UserInfoRepository.mid

        val data = http.get(BilibiliApi.getMyUserData).body<MyUserData>()

        UserInfoRepository.mid = data.mid
        mid = data.mid
        return data.mid
    }

    /**
     * 获取用户基础信息
     */
    private suspend fun getUserData(params: List<Pair<String, String>>) {
        val bean = http.get(BilibiliApi.userBaseDataPath) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }.body<UserBaseBean>()
        _userDataState.update {
            it.copy(
                face = bean.face,
                name = bean.name,
                sign = bean.sign,
                nicknameColor = if (bean.isVip) Color(bean.vip.nicknameColor.toColorInt()) else Color.Black
            )
        }
    }

    private suspend fun getWbiKey(mid: Long): List<Pair<String, String>> {
        val bean = http.get(BilibiliApi.token).body<UserNavDataBean>()
        val params = listOf(MID to mid.toString())
        return WbiUtils.getParamStr(
            params,
            bean.imgKey,
            bean.subKey
        )
    }
}

private const val TAG = "UserViewModel"

data class UserState(
    val mid: Long = 0,
    val face: String = "",
    val name: String = "",
    val sign: String = "",
    val nicknameColor: Color = Color.Black,
    val uname: String = "",
    val userid: String = "",
    val fans: Int = 0,
    val friend: Int = 0,
    val likes: Int = 0,
    val archive: Int = 0,
)
