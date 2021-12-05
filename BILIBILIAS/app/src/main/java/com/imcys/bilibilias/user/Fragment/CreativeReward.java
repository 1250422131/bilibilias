package com.imcys.bilibilias.user.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.Reply;
import com.imcys.bilibilias.home.AccountListActivity;
import com.imcys.bilibilias.home.BiliLoginActivity;
import com.imcys.bilibilias.user.UserActivity;
import com.imcys.bilibilias.user.UserVideo;

import java.util.ArrayList;
import java.util.List;

public class CreativeReward extends Fragment {


    private String Mid;
    private String Cookie;
    private Intent intent;
    private Object AsCookie;
    private SharedPreferences sharedPreferences;
    private WebView webView1;
    private AppCompatActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.creative_reward, container, false);
        webView1 = (WebView) view.findViewById(R.id.Creative_WebView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        AsCookie = sharedPreferences.getString("AsCookie", "0");

        //获取首页用户信息变量
        Cookie = UserActivity.cookie;
        Mid = UserActivity.mid;
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init("https://api.misakamoe.com/app/userCenter", AsCookie.toString());
    }

    //用户登录事件
    private void init(String LoginUrl, String Cookie) {

        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(LoginUrl, Cookie);


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

    class NewWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //添加Cookie获取操作
            CookieManager cookieManager = CookieManager.getInstance();
            /*
            cookie = cookieManager.getCookie(url);
            System.out.println(cookie);

             */
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候WebView打开，为false则系统浏览器或第三方浏览器打开。
            //如果要下载页面中的游戏或者继续点击网页中的链接进入下一个网页的话，重写此方法下，不然就会跳到手机自带的浏览器了，而不继续在你这个webview里面展现了
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (AppCompatActivity) context;

    }
}
