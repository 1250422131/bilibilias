package com.imcys.bilibilias;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.imcys.bilibilias.as.RankingActivity;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.imcys.bilibilias.user.AboutActivity;
import com.imcys.bilibilias.user.UserActivity;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class HomeActivity extends AppCompatActivity {

    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;
    private static int LJ;
    private String cookie;
    private String oauthKey;
    private String toKen;
    private String csrf;
    private String URL;
    private ProgressDialog pd2;
    private String mid;
    private String GxUrl = "https://api.misakaloli.com/app/bilibilias.php?type=json&edition=1.2";
    private String Edition = "1.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkPermission();

        //BUGly
        //Bugly.init(getApplicationContext(), "1bb190bc7d", false);

        //检测下载配置
        String DLPath = getExternalFilesDir("下载设置").toString()+"/Path.txt";
        File file = new File(DLPath);
        if(!file.exists()){
            try {
                BilibiliPost.fileWrite(DLPath,getExternalFilesDir("哔哩哔哩视频").toString()+"/");
                DLPath = getExternalFilesDir("下载设置").toString()+"/断点续传设置.txt";
                file = new File(DLPath);
                if(!file.exists()){
                    BilibiliPost.fileWrite(DLPath,"1");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String StrGx = HttpUtils.doGet(GxUrl,"");
                    try {
                        JSONObject jsonGxStr = new JSONObject(StrGx);
                        final String MD5 = jsonGxStr.getString("APKMD5");
                        final String CRC =  jsonGxStr.getString("APKToKenCR");
                        final String SHA = jsonGxStr.getString("APKToKen");
                        final String ID = jsonGxStr.getString("ID");
                        final String StrUrl = jsonGxStr.getString("url");
                        final String sha = apkVerifyWithSHA(HomeActivity.this,SHA);
                        System.out.println(sha);
                        System.out.println(SHA);
                        final String md5 = apkVerifyWithMD5(HomeActivity.this,MD5);
                        System.out.println(md5);
                        System.out.println(MD5);
                        final String crc = apkVerifyWithCRC(HomeActivity.this,CRC);
                        System.out.println(crc);
                        System.out.println(CRC);
                        if (ID.equals("1")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!sha.equals(SHA)){
                                        ScrollView ScrollView_Main = (ScrollView)findViewById(R.id.Home_MainLayout);
                                        ScrollView_Main.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "请下载正版", Toast.LENGTH_SHORT).show();
                                        Uri uri = Uri.parse(StrUrl);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }else if(!md5.equals(MD5)){
                                        ScrollView ScrollView_Main = (ScrollView)findViewById(R.id.Home_MainLayout);
                                        ScrollView_Main.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "请下载正版", Toast.LENGTH_SHORT).show();
                                        Uri uri = Uri.parse(StrUrl);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }else if(!crc.equals(CRC)){
                                        ScrollView ScrollView_Main = (ScrollView)findViewById(R.id.Home_MainLayout);
                                        ScrollView_Main.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "请下载正版", Toast.LENGTH_SHORT).show();
                                        Uri uri = Uri.parse(StrUrl);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else if (ID.equals("0")){
                            String fs = HttpUtils.doGet("https://api.misakaloli.com/app/bilibilias.php?type=json&edition="+Edition+"&SHA="+sha+"&MD5="+md5+"&CRC="+crc+"lj="+LJ,cookie);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //更新检测
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String StrGx = HttpUtils.doGet(GxUrl,cookie);
                    System.out.println(StrGx);
                    if (!GxUrl.equals("https://api.misakaloli.com/app/bilibilias.php?type=json&edition="+Edition)) {
                    } else {
                        JSONObject jsonGxStr = null;
                        String StrPd = null;
                        try {
                            jsonGxStr = new JSONObject(StrGx);
                            StrPd = jsonGxStr.getString("edition");
                            final String StrNr = jsonGxStr.getString("gxnotice");
                            final String StrUrl = jsonGxStr.getString("url");
                            if (StrPd.equals("1.2")) {
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                                                .setTitle("有新版本了")
                                                .setMessage(StrNr)
                                                .setPositiveButton("下载新版本", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Uri uri = Uri.parse(StrUrl);
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .setNeutralButton(null, null)
                                                .create();
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (Exception e) {

        }
        //公告声明
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String StrGx = HttpUtils.doGet(GxUrl,cookie);
                    try {
                        JSONObject jsonGxStr = new JSONObject(StrGx);
                        final String Gg = jsonGxStr.getString("notice");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                                        .setTitle("最新公告")
                                        .setMessage(Gg)
                                        .setPositiveButton("使用本程序代表同意上述内容", null)
                                        .setNeutralButton(null, null)
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }catch (Exception e) {
        }

        //检测用户是否登录
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginCheck();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }




    //布局事件区域 ↓ ↓ ↓
    public void GoUser(View view){
        Intent intent= new Intent();
        intent.setClass(HomeActivity.this, UserActivity.class);
        intent.putExtra("cookie",cookie);
        intent.putExtra("csrf",csrf);
        intent.putExtra("toKen",toKen);
        intent.putExtra("mid",mid);
        startActivity(intent);
    }

    public void GoVideoAs(View view){
        Intent intent= new Intent();
        intent.setClass(HomeActivity.this, VideoAsActivity.class);
        intent.putExtra("cookie",cookie);
        intent.putExtra("csrf",csrf);
        intent.putExtra("toKen",toKen);
        intent.putExtra("mid",mid);
        startActivity(intent);
    }

    public void goSet(View view){
        Intent intent= new Intent();
        intent.setClass(HomeActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void goRanking(View view){
        Intent intent= new Intent();
        intent.setClass(HomeActivity.this, RankingActivity.class);
        startActivity(intent);
    }





    //用户登录情况检测
    private void LoginCheck() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取必要信息内容
                final String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"token.txt";
                String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl","");
                //获取登录信息，同时捕获如果没有登录时，登录需要的数据和个人信息
                try {
                    JSONObject LoginQRJson = new JSONObject(likeStr);
                    LoginQRJson = LoginQRJson.getJSONObject("data");
                    URL = LoginQRJson.getString("url");
                    oauthKey = LoginQRJson.getString("oauthKey");
                    System.out.println(URL);
                    csrf = BilibiliPost.fileRead(csrfPath);
                    toKen = BilibiliPost.fileRead(ToKenPath);
                    cookie = BilibiliPost.fileRead(CookiePath);
                    System.out.println(toKen);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String pd = BilibiliPost.nav(toKen);
                if(pd.equals("0")){
                    final String UserNavStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/nav",cookie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //确定是登录状态，因为这个是主页，就不需要做其他操作了
                            JSONObject UserNavJson = null;
                            String UserInfo = null;
                            try {
                                UserNavJson = new JSONObject(UserNavStr);
                                UserNavJson = UserNavJson.getJSONObject("data");
                                mid = UserNavJson.getString("mid");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init("https://api.misakaloli.com/app/bilibiliasLogin.php?URL="+URL);
                            WebView webView1 = (WebView)findViewById(R.id.Home_WebView1);
                            webView1.setWebChromeClient(new WebChromeClient());
                            webView1.setWebViewClient(new NewWebViewClient());
                            ScrollView webLayout = (ScrollView)findViewById(R.id.Home_WebLayout);
                            ScrollView mainLayout = (ScrollView)findViewById(R.id.Home_MainLayout);
                            webLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    //用户登录事件
    private void init(String LoginUrl){
        WebView webView1 = (WebView)findViewById(R.id.Home_WebView1);
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

    public void NewLogin(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl","");
                JSONObject LoginQRJson = null;
                try {
                    LoginQRJson = new JSONObject(likeStr);
                    LoginQRJson = LoginQRJson.getJSONObject("data");
                    URL = LoginQRJson.getString("url");
                    oauthKey = LoginQRJson.getString("oauthKey");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(URL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String QRUrl = "https://api.qrserver.com/v1/create-qr-code/?data="+URL;
                        URL = "https://api.misakaloli.com/app/bilibiliasLogin.php?URL="+URL;
                        init(URL);
                        URL = QRUrl;
                        WebView webView1 = (WebView)findViewById(R.id.Home_WebView1);
                        webView1.setWebChromeClient(new WebChromeClient());
                        webView1.setWebViewClient(new NewWebViewClient());
                        ScrollView webLayout = (ScrollView)findViewById(R.id.WebLayout);
                        webLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    public void UpLogin(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cookie = HttpUtils.getCookie("oauthKey=" + oauthKey,"http://passport.bilibili.com/qrcode/getLoginInfo");
                    System.out.println(cookie);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(cookie.length()>45){
                    final String UserNavStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/nav",cookie);
                    //这里是造成之前个人主页无数据的原因，mid没有抓到
                    JSONObject UserNavJson = null;
                    try {
                        UserNavJson = new JSONObject(UserNavStr);
                        UserNavJson = UserNavJson.getJSONObject("data");
                        mid = UserNavJson.getString("mid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toKen ="SESSDATA="+sj(cookie,"SESSDATA=",";");
                            csrf = sj(cookie,"bili_jct=",";");
                            System.out.println(toKen);
                            String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                            String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"token.txt";
                            String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                            try {
                                BilibiliPost.fileWrite(CookiePath,cookie);
                                BilibiliPost.fileWrite(ToKenPath,toKen);
                                BilibiliPost.fileWrite(csrfPath,csrf);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ScrollView webLayout = (ScrollView)findViewById(R.id.Home_WebLayout);
                            webLayout.setVisibility(View.GONE);
                            ScrollView mainLayout = (ScrollView)findViewById(R.id.Home_MainLayout);
                            mainLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "未登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void UpLoginNew(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl","");
                JSONObject LoginQRJson = null;
                try {
                    LoginQRJson = new JSONObject(likeStr);
                    LoginQRJson = LoginQRJson.getJSONObject("data");
                    URL = LoginQRJson.getString("url");
                    oauthKey = LoginQRJson.getString("oauthKey");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(URL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                        String QRUrl = "https://api.qrserver.com/v1/create-qr-code/?data="+URL;
                        URL = "https://api.misakaloli.com/app/bilibiliasLogin.php?URL="+URL;
                        init(URL);
                        URL = QRUrl;
                        WebView webView1 = (WebView)findViewById(R.id.Home_WebView1);
                        webView1.setWebChromeClient(new WebChromeClient());
                        webView1.setWebViewClient(new NewWebViewClient());
                        ScrollView webLayout = (ScrollView)findViewById(R.id.Home_WebLayout);
                        webLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }


    public void QRDownload(View view){
        //二维码下载
        pd2 = ProgressDialog.show(HomeActivity.this, "提示", "正在拉取数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap QRBitmap = BilibiliPost.returnBitMap("https://api.qrserver.com/v1/create-qr-code/?data="+URL);
                savePhoto(HomeActivity.this,QRBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "下载完成,自动跳转扫码页面", Toast.LENGTH_SHORT).show();
                        pd2.cancel();
                        SetQR();
                    }
                });
            }
        }).start();
    }

    private void SetQR(){
        try {
            Context context = HomeActivity.this;
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("bilibili://qrscan"));
            PackageManager packageManager= context.getPackageManager();
            ComponentName componentName=intent.resolveActivity(packageManager);
            if (componentName!=null){
                context.startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "呜哇，好像没有安装B站", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转B站扫码入口
    public void SetQR(View view){
        SetQR();
    }

    //图片保存方案
    //保存到本地
    public static void savePhoto(Context context, Bitmap bitmap){
        File photoDir = new File(Environment.getExternalStorageDirectory(),"BILIBILIAS");
        if (!photoDir.exists()){
            photoDir.mkdirs();
        }
        String fileName = "BILIBILIAS扫码.jpg";
        File photo = new File(photoDir,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(photo);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        updatePhotoMedia(photo,context);
    }

    //更新图库
    private static void updatePhotoMedia(File file ,Context context){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    class NewWebViewClient extends WebViewClient{
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


    public static String sj(String str, String start, String end)
    {
        if (str.contains(start) && str.contains(end))
        {
            str = str.substring(str.indexOf(start) + start.length());
            return str.substring(0, str.indexOf(end));
        }
        return "";
    }



    //程序基层保护或者必要的方法均列举在下面
    //权限获取方法
    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, PERMISSION_REQUEST);
        }
    }


    //底层程序加固 -> 防止程序被修改从多个角度检测安装包完整性
    /**
     * 通过检查签名文件classes.dex文件的哈希值来判断代码文件是否被篡改
     *
     * @param orginalSHA 原始Apk包的SHA-1值
     */
    public static String apkVerifyWithSHA(Context context, String orginalSHA) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
            BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);
            fis.close();
            return sha;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过检查apk包的MD5摘要值来判断代码文件是否被篡改
     *
     * @param orginalMD5 原始Apk包的MD5值
     */
    public static String  apkVerifyWithMD5(Context context, String orginalMD5) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        System.out.println("路径长度");
        System.out.println(apkPath.length());
        LJ = apkPath.length();
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
            BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);
            fis.close();
            return sha;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过检查classes.dex文件的CRC32摘要值来判断文件是否被篡改
     *
     * @param orginalCRC 原始classes.dex文件的CRC值
     */
    public static String apkVerifyWithCRC(Context context, String orginalCRC) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            ZipFile zipFile = new ZipFile(apkPath);
            ZipEntry dexEntry = zipFile.getEntry("classes.dex"); // 读取ZIP包中的classes.dex文件
            String dexCRC = String.valueOf(dexEntry.getCrc()); // 得到classes.dex文件的CRC值
            return dexCRC;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





}