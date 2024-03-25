package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.base.utils.asToast

object LoginViewModel {

    fun toBiliAgreement(view: View) {
        asToast(view.context, "无论如何，你都在间接使用B站")
        privacyAgreementWebView(
            view.context, "https://www.bilibili.com/blackboard/topic/activity-cn8bxPLzz.html",
            "B站账户需要遵守的协议列表", "使用则代表同意"
        )
    }

    fun toBilibiliAsAgreement(view: View) {
        privacyAgreementWebView(
            view.context,
            "https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865",
            "BILIBILIAS隐私政策",
            "使用则代表同意"
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun privacyAgreementWebView(
        context: Context, url: String, title: String,
        positiveText: String,
    ) {
        val webView = WebView(context)
        val client = WebChromeClient()
        StatService.trackWebView(context, webView, client)

        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = false
        webView.loadUrl(url)
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1500)
        linearLayout.addView(webView, layoutParams)
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(title)
            .setPositiveButton(positiveText) { dialog, _ ->
                webView.destroy()
                dialog.cancel()
            }.show()
    }

}
