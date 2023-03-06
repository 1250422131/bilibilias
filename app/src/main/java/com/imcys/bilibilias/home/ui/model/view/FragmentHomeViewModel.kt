package com.imcys.bilibilias.home.ui.model.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.core.content.edit
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.xiaojinzi.component.impl.Router
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.system.exitProcess

class FragmentHomeViewModel {


    fun goToPrivacyPolicy(view: View) {
        val uri =
            Uri.parse("https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865")
        val intent = Intent(Intent.ACTION_VIEW, uri);
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
        val intent = Intent(Intent.ACTION_VIEW, uri);
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
        val intent = Intent(Intent.ACTION_VIEW, uri);
        view.context.startActivity(intent)
    }

    fun goToCommunity(view: View) {
        val uri = Uri.parse("https://support.qq.com/product/337496")
        val intent = Intent(Intent.ACTION_VIEW, uri);
        view.context.startActivity(intent)
    }


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
                val cookie = view.context.getSharedPreferences("data", Context.MODE_PRIVATE)
                    .getString("cookies", "")
                val biliJct = view.context.getSharedPreferences("data", Context.MODE_PRIVATE)
                    .getString("bili_jct", "")

                HttpUtils.addHeader("cookie", cookie!!)
                    .addParam("biliCSRF", biliJct!!)
                    .post(BilibiliApi.exitLogin, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                        }
                        override fun onResponse(call: Call, response: Response) {

                            val sharedPreferences: SharedPreferences =
                                view.context.getSharedPreferences("data", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putLong("mid", 0)
                            editor.putString("cookies", "")
                            editor.putString("bili_jct", "")
                            editor.apply()

                            asToast(view.context,"清除完成，请关闭后台重新进入")
                        }

                    })
            },
            negativeButtonClickListener = {}
        ).show()
    }


}