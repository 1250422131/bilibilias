package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.baidu.mobstat.StatService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.base.utils.asToast


class LoginViewModel {


    lateinit var context: Context
    private lateinit var loginQRDialog: BottomSheetDialog
    private lateinit var bottomSheetDialog:BottomSheetDialog

    fun tip(view: View){
        asToast(context,"云端账户正在重建")
    }



    fun toBiliAgreement(view: View) {
        biliAgreement(view.context)
    }

    fun toBilibiliAsAgreement(view: View) {
        bilibiliAsAgreement(view.context)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun biliAgreement(context: Context) {
        asToast(context,"无论如何，你都在间接使用B站")
        val lLayout = LinearLayout(context)
        lLayout.orientation = LinearLayout.VERTICAL


        val Privacy = WebView(context)
        val webChromeClient = object : WebChromeClient() {
        }
        Privacy.webChromeClient = webChromeClient

        //监听
        StatService.trackWebView(context, Privacy, webChromeClient)

        Privacy.settings.javaScriptCanOpenWindowsAutomatically =
            true //设置js可以直接打开窗口，如window.open()，默认为false
        Privacy.settings.javaScriptEnabled = true //是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        Privacy.settings.loadWithOverviewMode = true //和setUseWideViewPort(true)一起解决网页自适应问题
        Privacy.settings.domStorageEnabled = true //DOM Storage 重点是设置这个
        Privacy.settings.allowFileAccess = false
        Privacy.loadUrl("https://www.bilibili.com/blackboard/topic/activity-cn8bxPLzz.html")
        val lParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            1500) //这个属性是设置空间的长宽，其实还可以设置其他的控件的其他属性；
        lLayout.addView(Privacy, lParams)
        val builder = AlertDialog.Builder(context)
        builder.setView(lLayout)
        builder.setCancelable(false)
        builder.setTitle("B站账户需要遵守的协议列表")
        builder.setPositiveButton("使用则代表同意") { dialog, which ->
            Privacy.destroy()
            dialog.cancel()
        }
        builder.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun bilibiliAsAgreement(context: Context) {
        val lLayout = LinearLayout(context)
        lLayout.orientation = LinearLayout.VERTICAL
        val Privacy = WebView(context)
        val webChromeClient = object : WebChromeClient() {
        }
        Privacy.webChromeClient = webChromeClient

        //监听
        StatService.trackWebView(context, Privacy, webChromeClient)
        Privacy.settings.javaScriptCanOpenWindowsAutomatically =
            true //设置js可以直接打开窗口，如window.open()，默认为false
        Privacy.settings.javaScriptEnabled = true //是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        Privacy.settings.loadWithOverviewMode = true //和setUseWideViewPort(true)一起解决网页自适应问题
        Privacy.settings.domStorageEnabled = true //DOM Storage 重点是设置这个
        Privacy.settings.allowFileAccess = false
        Privacy.loadUrl("https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865")
        val lParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            1500) //这个属性是设置空间的长宽，其实还可以设置其他的控件的其他属性；
        lLayout.addView(Privacy, lParams)
        val builder = AlertDialog.Builder(context)
        builder.setView(lLayout)
        builder.setCancelable(false)
        builder.setTitle("B站账户需要遵守的协议列表")
        builder.setPositiveButton("使用则代表同意"
        ) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
}