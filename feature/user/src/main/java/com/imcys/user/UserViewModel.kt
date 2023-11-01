package com.imcys.user

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.imcys.model.UserCreateCollectionBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor() : ViewModel() {
    private val _userDataState = MutableStateFlow(UserState())
    val userDataState = _userDataState.asStateFlow()

    private var mid = 0L

    init {
        getUserInformation()
    }

    private fun getUserInformation() {
        // launchIO {
        //     val mid = getMyUserData()
        //
        //     val params = getWbiKey(mid)
        //
        //     launch { getUserData(params) }
        //     launch { getUserCardBean(params) }
        //     launch { getUpStat(mid) }
        // }
    }

    /**
     * 收藏列表
     */
    fun loadCollectionList() {
        // launchIO {
        //     val bean = http.get(BilibiliApi.userAllFavorites) {
        //         parameter("up_mid", mid)
        //     }.body<UserCreateCollectionBean>()
        //
        //     }
        }

    fun loadCollectionData(id: Int, pn: Int, ps: Int = 20) {
      // view  launchIO {
      //       val bean = http.get("BilibiliApi.getFavoritesContentList") {
      //           // parameter("media_id", id)
      //           // parameter("pn", pn)
      //           // parameter("ps", ps.coerceAtMost(20))
      //       }.body<Collections>()
      //       _userDataState.update {
      //           it.copy(medias = (it.medias + bean.medias).toImmutableList())
      //       }
      //   }
    }

    /**
     * 获取用户状态信息
     */
    private suspend fun getUpStat(mid: Long) {
        // val bean = http.get("BilibiliApi.userUpStat") {
        //     "parameter(MID, mid)"
        // }.body<UpStatBean>()
        // _userDataState.update {
        //     it.copy(likes = bean.likes, archive = bean.archive.view)
        // }
    }

    /**
     * 获取用户卡片信息
     */
    private suspend fun getUserCardBean(params: List<Pair<String, String>>) {
        // val bean = http.get("BilibiliApi.getUserCardPath") {
        //     params.forEach { (k, v) ->
        //         "parameter(k, v)"
        //     }
        // }.body<UserCardBean>()
        // _userDataState.update {
        //     it.copy(fans = bean.card.fans, friend = bean.card.friend)
        // }
    }

    private suspend fun getMyUserData(): Long {

      return 0
    }

    /**
     * 获取用户基础信息
     */
    private suspend fun getUserData(params: List<Pair<String, String>>) {
        // val bean = http.get("BilibiliApi2.userSpaceDetails") {
        //     params.forEach { (k, v) ->
        //         "parameter(k, v)"
        //     }
        // }.body<UserSpaceInformation>()
        // _userDataState.update {
        //     it.copy(
        //         face = bean.face,
        //         name = bean.name,
        //         sign = bean.sign,
        //         nicknameColor = if (bean.isVip) Color(bean.vip.nicknameColor.toColorInt()) else Color.Black
        //     )
        // }
    }

    private suspend fun getWbiKey(mid: Long): List<Pair<String, String>> {
        return emptyList()
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

    val collectionList: ImmutableList<UserCreateCollectionBean.Collection> = persistentListOf(),
    // val medias: ImmutableList<Collections.Media> = persistentListOf(),

    )
