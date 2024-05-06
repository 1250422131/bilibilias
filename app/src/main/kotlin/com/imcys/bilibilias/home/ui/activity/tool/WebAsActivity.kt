package com.imcys.bilibilias.home.ui.activity.tool

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityWebAsBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: 提取公共方法
@AndroidEntryPoint
class WebAsActivity : BaseActivity<ActivityWebAsBinding>() {
    override val layoutId: Int = R.layout.activity_web_as

//    @Inject
//    lateinit var asCookiesStorage: AsCookiesStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            // 设置返回按钮可用
            setSupportActionBar(webAsMaterialToolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }

            webAsTopLy
        }
    }

    override fun initView() {
        loadWebView()
    }
    private fun loadWebView() {
        binding.apply {
            // 不缓存
            webAsWebView.settings.javaScriptEnabled = true
            webAsWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webAsWebView.settings.allowFileAccess = false
            CookieSyncManager.createInstance(this@WebAsActivity)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.removeAllCookie()
            lifecycleScope.launch {
//                cookieManager.setCookie("https://bilibili.com", asCookiesStorage.getAllCookies())
            }
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
        // 配置完成事件
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.tool_web_toolbar_menu_finish -> {
                val thisUrl = binding.webAsWebView.url
                thisUrl?.let { HomeActivity.actionStart(this, it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
