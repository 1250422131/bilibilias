package com.imcys.bilibilias.home;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mobstat.StatService;
import com.imcys.bilibilias.R;


public class VersionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_version);
        init("https://docs.qq.com/doc/DVXZNWUVFakxEQ2Va?pub=1&dver=2.1.27292865");
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    private void init(String LoginUrl) {
        WebView webView1 = (WebView) findViewById(R.id.Version_WebView);
        //WebView加载web资源
        webView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false

        webView1.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞

        webView1.getSettings().setSupportZoom(true);//是否可以缩放，默认true

        webView1.getSettings().setBuiltInZoomControls(false);//是否显示缩放按钮，默认false

        webView1.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式

        webView1.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题

        webView1.getSettings().setAppCacheEnabled(true);//是否使用缓存

        webView1.getSettings().setDomStorageEnabled(true);//DOM Storage 重点是设置这个

        webView1.getSettings().setAllowFileAccess(false);

        webView1.loadUrl(LoginUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

}
