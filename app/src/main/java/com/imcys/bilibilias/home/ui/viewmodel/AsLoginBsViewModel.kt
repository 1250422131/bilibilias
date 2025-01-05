package com.imcys.bilibilias.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.common.base.model.common.IPostBody
import com.imcys.bilibilias.common.base.model.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import javax.inject.Inject

@HiltViewModel
class AsLoginBsViewModel @Inject constructor() : ViewModel() {

    data class AsLoginInfo(
        val email: String,
        val password: String,
        val security: String,
    ) : IPostBody

    /**
     * 提交云端cookie
     * @param context HomeActivity
     */

    data class BiliBiliCookieInfo(
        val name: String,
        val level: Int,
        val face: String,
        val cookie: String,
        val type: Int = 1,
    ) : IPostBody

}
