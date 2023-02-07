package com.imcys.bilibilias.home.ui.activity.tool

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityWebAsBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class WebAsActivity : BaseActivity() {
    private lateinit var webAsBinding: ActivityWebAsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //视图加载
        webAsBinding =
            DataBindingUtil.setContentView<ActivityWebAsBinding?>(this, R.layout.activity_web_as)
                .apply {
                    //设置返回按钮可用
                    setSupportActionBar(webAsMaterialToolbar)
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(true)
                        setHomeButtonEnabled(true)
                    }

                    webAsTopLy.addStatusBarTopPadding()
                }


        loadWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        webAsBinding.apply {

            //不缓存
            webAsWebView.settings.javaScriptEnabled = true
            webAsWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webAsWebView.settings.allowFileAccess = false

            CookieSyncManager.createInstance(this@WebAsActivity)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.removeAllCookie()
            //注入cookie
            cookieManager.setCookie("https://bilibili.com", asUser.cookie)
            cookieManager.flush()
            webAsWebView.loadUrl("https://m.bilibili.com")

            webAsWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_web_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //配置完成事件
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.tool_web_toolbar_menu_finish -> {
                val thisUrl = webAsBinding.webAsWebView.url
                thisUrl?.let { HomeActivity.actionStart(this, it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}