package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.baidu.mobstat.StatService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.utils.asToast

class LoginViewModel : ViewModel() {

    private lateinit var loginQRDialog: BottomSheetDialog
    private lateinit var bottomSheetDialog: BottomSheetDialog

    fun toBiliAgreement(view: View) {
        biliAgreement(view.context)
    }

    fun toBilibiliAsAgreement(view: View) {
        bilibiliAsAgreement(view.context)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun biliAgreement(context: Context) {
        asToast(context, context.getString(R.string.app_login_view_biliagreement_context))

        val lLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        val privacy = WebView(context).apply {
            val webChromeClient = object : WebChromeClient() {
            }

            // 监听
            StatService.trackWebView(context, this, webChromeClient)

            settings.javaScriptCanOpenWindowsAutomatically =
                true // 设置js可以直接打开窗口，如window.open()，默认为false
            settings.javaScriptEnabled = true // 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
            settings.loadWithOverviewMode = true // 和setUseWideViewPort(true)一起解决网页自适应问题
            settings.domStorageEnabled = true // DOM Storage 重点是设置这个
            settings.allowFileAccess = false
            loadUrl("https://www.bilibili.com/blackboard/topic/activity-cn8bxPLzz.html")
        }

        val lParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1500,
        ) // 这个属性是设置空间的长宽，其实还可以设置其他的控件的其他属性；
        lLayout.addView(privacy, lParams)

        AlertDialog.Builder(context).apply {
            setView(lLayout)
            setCancelable(false)
            setTitle(context.getString(R.string.app_login_view_biliagreement_alert_title))
            setPositiveButton(context.getString(R.string.app_login_view_biliagreement_alert_button_text)) { dialog, _ ->
                privacy.destroy()
                dialog.cancel()
            }
        }.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun bilibiliAsAgreement(context: Context) {
        val lLayout = LinearLayout(context)
        lLayout.orientation = LinearLayout.VERTICAL
        val Privacy = WebView(context)
        val webChromeClient = object : WebChromeClient() {
        }
        Privacy.webChromeClient = webChromeClient

        // 监听
        StatService.trackWebView(context, Privacy, webChromeClient)
        Privacy.settings.javaScriptCanOpenWindowsAutomatically =
            true // 设置js可以直接打开窗口，如window.open()，默认为false
        Privacy.settings.javaScriptEnabled = true // 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        Privacy.settings.loadWithOverviewMode = true // 和setUseWideViewPort(true)一起解决网页自适应问题
        Privacy.settings.domStorageEnabled = true // DOM Storage 重点是设置这个
        Privacy.settings.allowFileAccess = false
        Privacy.loadUrl("https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865")
        val lParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1500,
        ) // 这个属性是设置空间的长宽，其实还可以设置其他的控件的其他属性；
        lLayout.addView(Privacy, lParams)
        val builder = AlertDialog.Builder(context)
        builder.setView(lLayout)
        builder.setCancelable(false)
        builder.setTitle(context.getString(R.string.app_login_view_bilibiliasagreement_title))
        builder.setPositiveButton(
            context.getString(R.string.app_login_view_bilibiliasagreement_text),
        ) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
}
