package com.imcys.bilibilias.home.ui.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.extend.launchUI
import javax.inject.Inject

class FragmentHomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var networkService: NetworkService

    fun logoutLogin(view: View) {
        DialogUtils.dialog(
            view.context,
            "退出登录",
            "确定要退出登录了吗？",
            "是的",
            "点错了",
            true,
            positiveButtonClickListener =
            {
//                val csrf = asCookiesStorage.getCookieValue("bili_jct")
                TODO("重构")
                launchUI {
//                    networkService.exitUserLogin(csrf ?: "")

                    BaseApplication.dataKv.apply {
                        encode("mid", 0)
                    }

                    asToast(view.context, "清除完成，请关闭后台重新进入")
                }
            },
            negativeButtonClickListener = {},
        ).show()
    }
}
