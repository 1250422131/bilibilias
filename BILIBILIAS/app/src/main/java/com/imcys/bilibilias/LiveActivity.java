package com.imcys.bilibilias;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.xutils.x;

import java.util.regex.Pattern;

public class LiveActivity  extends AppCompatActivity {
    private ProgressDialog pd2;
    private ProgressDialog progressDialog;
    private String cookie;
    private String oauthKey;
    private String toKen;
    private String csrf;
    private String URL;
    private String StrData;
    private String cid;
    private String url;
    private String Str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        x.view().inject(this);//绑定注解
    }

    public void onCid(View view) {
        pd2 = ProgressDialog.show(this, "提示", "正在拉取数据");
        EditText EditTextLive1 = (EditText)findViewById(R.id.Live_EditText1);
        StrData = EditTextLive1.getText().toString();
        System.out.println(StrData);
        new cidUrl().start();
    }

    public class cidUrl extends Thread {
        @Override
        public void run(){
            if(isENChar(StrData)){
                if(StrData.contains("https")||StrData.contains("http")){
                    Str = HttpUtils.doGet(StrData,"");
                    cid = sj(StrData,"window.__NEPTUNE_IS_MY_WAIFU__=","</script>");
                    cid = sj(cid,"room_id\":",",\"");
                    Str = HttpUtils.doGet("https://api.misakaloli.com/bilibili/live.php?id=" + cid, "");
                    if(Str.equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd2.dismiss();
                                Toast.makeText(getApplicationContext(), "看起来没有找到呢", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd2.dismiss();
                                init("https://ycx.mxjs.xyz/?live="+cid);
                                WebView webView1 = (WebView)findViewById(R.id.Live_WebView1);
                                webView1.setWebChromeClient(new WebChromeClient());
                                webView1.setWebViewClient(new LiveActivity.NewWebViewClient());
                                ScrollView webLayout = (ScrollView)findViewById(R.id.Live_ScrollView1);
                                LinearLayout Live_LinearLayout1 = (LinearLayout)findViewById(R.id.Live_LinearLayout1);
                                Live_LinearLayout1.setVisibility(View.GONE);
                                webLayout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }else {
                Str = HttpUtils.doGet("https://api.misakaloli.com/bilibili/live.php?id=" + StrData, "");
                System.out.println(Str);
                cid = StrData;
                if (Str.equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd2.dismiss();
                            Toast.makeText(getApplicationContext(), "看起来没有找到呢", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd2.dismiss();
                            init("https://ycx.mxjs.xyz/?live="+cid);
                            WebView webView1 = (WebView) findViewById(R.id.Live_WebView1);
                            webView1.setWebChromeClient(new WebChromeClient());
                            webView1.setWebViewClient(new LiveActivity.NewWebViewClient());
                            ScrollView webLayout = (ScrollView) findViewById(R.id.Live_ScrollView1);
                            LinearLayout Live_LinearLayout1 = (LinearLayout) findViewById(R.id.Live_LinearLayout1);
                            Live_LinearLayout1.setVisibility(View.GONE);
                            webLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }
    }

    public boolean isENChar(String string) {
        boolean flag = false;
        Pattern p = Pattern.compile("[a-zA-z]");
        if(p.matcher(string).find()) {
            flag = true;
        }
        return flag;
    }

    //截取字符串方法
    public static String sj(String str, String start, String end)
    {
        if (str.contains(start) && str.contains(end))
        {
            str = str.substring(str.indexOf(start) + start.length());
            return str.substring(0, str.indexOf(end));
        }
        return "";
    }

    private void init(String LoginUrl){
        WebView webView1 = (WebView)findViewById(R.id.Live_WebView1);
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
        webView1.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
    class NewWebViewClient extends WebViewClient{

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
            // super.onReceivedSslError(view, handler, error);

            // 接受所有网站的证书，忽略SSL错误，执行访问网页
            handler.proceed();
        }

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

}

