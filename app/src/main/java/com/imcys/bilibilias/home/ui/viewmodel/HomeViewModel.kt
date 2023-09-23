package com.imcys.bilibilias.home.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.imcys.bilibilias.base.utils.openUri
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.config.CookieRepository
import com.imcys.bilibilias.common.base.repository.login.LoginRepository
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.xiaojinzi.component.impl.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

    fun goToPrivacyPolicy(view: View) {
        val uri =
            Uri.parse("https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        view.context.startActivity(intent)
    }

    fun goToRoam(view: View) {
        // 跳转
        Router
            .with(view.context)
            .hostAndPath(hostAndPath = ARouterAddress.LiveStreamActivity).forward()
    }

    /**
     * 更新信息
     */
    fun goToNewVersionDoc(context: Context) {
        val uri = Uri.parse("https://docs.qq.com/doc/DVXZNWUVFakxEQ2Va")
        context.openUri(uri)
    }

    /**
     * 贡献
     */
    fun goToDedicatePage(view: View) {
        val intent = Intent(view.context, DedicateActivity::class.java)
        view.context.startActivity(intent)
    }

    /**
     * 捐款
     */
    fun goToDonate(view: View) {
        val intent = Intent(view.context, DonateActivity::class.java)
        view.context.startActivity(intent)
    }

    /**
     * 捐款列表
     */
    fun goToDonateList(context: Context) {
        val uri = Uri.parse("https://api.misakamoe.com/as-donate.html")
        context.openUri(uri)
    }

    /**
     * 社区
     */
    fun goToCommunity(context: Context) {
        val uri = Uri.parse("https://support.qq.com/product/337496")
        context.openUri(uri)
    }

    fun logoutLogin() {
        val cookie = CookieRepository.sessionData

        if (cookie.isNullOrEmpty()) {
            return
        }
        launchIO {
            loginRepository.logout()
            CookieRepository.clearCookies()
        }
    }
}
