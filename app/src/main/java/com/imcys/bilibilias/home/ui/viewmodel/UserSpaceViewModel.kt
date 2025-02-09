package com.imcys.bilibilias.home.ui.viewmodel

import android.database.Cursor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.base.network.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSpaceViewModel @Inject constructor(
    private val networkService: NetworkService,
) : ViewModel() {
    private var cursor = Cursor()
    val userSpaceUiState = MutableStateFlow(UserSpaceUiState())

    init {
        getUserInfo()
        getHistory()
    }

    fun getHistory() {
        viewModelScope.launch {
            val bean = networkService.getPlayHistory(cursor.max, cursor.viewAt).data
            cursor = Cursor(bean.cursor.max, bean.cursor.view_at)
            userSpaceUiState.update {
                it.copy(
                    histories = it.histories + bean.list.map {
                        History(
                            title = it.title,
                            cover = it.cover,
                            author = it.author_name,
                            epid = 0,
                            bvid = it.history.bvid,
                            cid = it.history.cid,
                            oid = it.history.oid
                        )
                    }
                )
            }
        }
    }

    private fun getUserInfo(): Unit {
        viewModelScope.launch {
            val info = networkService.getUserNavInfo().data
            userSpaceUiState.update { it.copy(name = info.uname, face = info.face) }
        }
    }

    private data class Cursor(
        val max: Long = 0,
        val viewAt: Long = 0,
    )
}

data class UserSpaceUiState(
    val name: String = "",
    val face: String = "",
    val histories: List<History> = emptyList()
)

data class History(
    val title: String,
    val cover: String,
    val author: String,
    val epid: Long,
    val bvid: String,
    val cid: Long,
    val oid: Long,
)