package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import com.imcys.bilibilias.utils.HttpUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginQRModel {


    var binding: DialogLoginQrBottomsheetBinding? = null
    var activity: Activity? = null
    var loginTip = ""

    lateinit var responseResult: (Int, LoginStateBean) -> Unit


    /**
     * 完成登录方法
     * @param view View
     * @param qrcode_key String
     */
    fun finishLogin(view: View, qrcode_key: String) {
        val bottomSheetDialog = activity?.let { DialogUtils.loadDialog(it) }
        bottomSheetDialog?.show()

        //登录完成
        HttpUtils.get(
            BilibiliApi.getLoginStatePath + "?qrcode_key=" + qrcode_key,
            object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    bottomSheetDialog?.cancel()
                }

                //————————————————————————————————————————————————
                @SuppressLint("CommitPrefEdits")
                override fun onResponse(call: Call, response: Response) {
                    //数据解析
                    val loginStateBean: LoginStateBean =
                        Gson().fromJson(response.body?.string(), LoginStateBean::class.java)
                    //————————————————————————————————————————————————
                    //关闭加载弹窗
                    bottomSheetDialog?.cancel()
                    //更新UI线程
                    activity?.runOnUiThread {

                        //登录成功则去储存cookie
                        if (loginStateBean.data.code == 0) {
                            loginSuccessOp(loginStateBean, response)
                        } else {
                            //展示登录结果
                            val loginQRModel = binding?.loginQRModel!!
                            loginQRModel.loginTip = loginStateBean.data.message
                            binding?.loginQRModel = loginQRModel

                        }

                        //将登录完成事件返回给Fragment
                        responseResult(loginStateBean.data.code, loginStateBean)
                    }


                }
            }
        )


    }


    /**
     * 重新加载二维码视图
     * @param view View
     * @param loginQrcodeDataBean DataBean
     */
    fun reloadLoginQR(view: View, loginQrcodeDataBean: LoginQrcodeBean.DataBean) {

        HttpUtils.get(
            BilibiliApi.getLoginQRPath,
            LoginQrcodeBean::class.java
        ) {
            loginQrcodeDataBean.url = URLEncoder.encode(it.data.url, "UTF-8")
            loginQrcodeDataBean.qrcode_key = it.data.qrcode_key
            binding?.dataBean = loginQrcodeDataBean
        }

    }


    /**
     * 登录成功后储存cookie等资源
     * @param loginStateBean LoginStateBean
     * @param response Response
     */
    @SuppressLint("CommitPrefEdits")
    fun loginSuccessOp(loginStateBean: LoginStateBean, response: Response) {

        val sharedPreferences: SharedPreferences =
            activity!!.getSharedPreferences("data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("refreshToken", loginStateBean.data.refresh_token)
        var cookies = ""
        // 创建 Pattern 对象
        val patternSESSDATA = "SESSDATA=(.*?);"
        val rSESSDATA: Pattern = Pattern.compile(patternSESSDATA)
        val patternBiliJct = "bili_jct=(.*?);"
        val rBiliJct: Pattern = Pattern.compile(patternBiliJct)


        response.headers.values("Set-Cookie").forEach {
            cookies += it
            var m = rSESSDATA.matcher(it)

            if (m.find()) {
                editor.putString("SESSDATA", m.group(1))
            }

            m = rBiliJct.matcher(it)

            if (m.find()) {
                editor.putString("bili_jct", m.group(1))
            }
        }
        editor.putString("cookies", cookies)
        editor.putString("refreshToken", loginStateBean.data.refresh_token)
        editor.apply()
    }

}