package com.imcys.bilibilias.home.ui.model.view

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.xiaojinzi.component.impl.Router
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class FragmentHomeViewModel : ViewModel() {


    fun goToPrivacyPolicy(view: View) {
        val uri =
            Uri.parse("https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        view.context.startActivity(intent)
    }

    fun goToRoam(view: View) {
        //跳转
        Router
            .with(view.context)
            .hostAndPath(hostAndPath = ARouterAddress.LiveStreamActivity).forward()
    }

    fun goToNewVersionDoc(view: View) {
        val uri = Uri.parse("https://docs.qq.com/doc/DVXZNWUVFakxEQ2Va")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        view.context.startActivity(intent)
    }

    fun goToDedicatePage(view: View) {
        val intent = Intent(view.context, DedicateActivity::class.java)
        view.context.startActivity(intent)
    }

    fun goToDonate(view: View) {
        val intent = Intent(view.context, DonateActivity::class.java)
        view.context.startActivity(intent)
    }

    fun toDonateList(view: View) {
        val uri = Uri.parse("https://api.misakamoe.com/as-donate.html")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        view.context.startActivity(intent)
    }

    fun goToCommunity(view: View) {
        val uri = Uri.parse("https://support.qq.com/product/337496")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        view.context.startActivity(intent)
    }


    fun logoutLogin(view: View) {

        val cookie = BaseApplication.dataKv.decodeString("cookies")

        // cookie存在空隐患
        if (cookie.isNullOrEmpty()){
            asToast(view.context,"你还没登录噢")
            return
        }

        DialogUtils.dialog(
            view.context,
            "退出登录",
            "确定要退出登录了吗？",
            "是的",
            "点错了",
            true,
            positiveButtonClickListener =
            {
                val biliJct = BaseApplication.dataKv.decodeString("bili_jct")

                HttpUtils.addHeader("cookie", cookie)
                    .addParam("biliCSRF", biliJct!!)
                    .post(BilibiliApi.exitLogin, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                        }

                        override fun onResponse(call: Call, response: Response) {

                            BaseApplication.dataKv.apply {
                                encode("mid", 0)
                                encode("cookies", "")
                                encode("bili_jct", "")
                            }


                            asToast(view.context, "清除完成，请关闭后台重新进入")
                        }

                    })
            },
            negativeButtonClickListener = {}
        ).show()
    }


}