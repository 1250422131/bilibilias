package com.imcys.bilibilias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.imcys.bilibilias.user.UserActivity;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;

    private ProgressDialog pd2;
    private ProgressDialog progressDialog;
    private LinearLayout LinearLayout1;
    private ScrollView ScrollView1;
    private String ImageUrl;
    private String type;
    private String Title;
    private String bvid;
    private String jxUrl;
    private String aid;
    private String up;
    private ImageView ImageView1;
    private TextView TextView1;
    private TextView TextView2;
    private Callback.Cancelable cancelable;
    private String StrData;
    public static String cookie;
    private String GxUrl = "https://api.misakaloli.com/app/bilibilias.php?type=json&edition=1.0";
    private String oauthKey;
    private String toKen;
    private String csrf;
    private String URL;
    private List<String> list = new ArrayList<String>();
    private List<String> listVideo = new ArrayList<String>();
    private List<String> listCode = new ArrayList<String>();
    private Spinner mProSpinner = null;
    private String cid;
    private String qn;
    private String fnval;
    private String videoType;
    private static int LJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);//绑定注解
        //检测动态权限
        checkPermission();

        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String StrGx = HttpUtils.doGet(GxUrl,cookie);
                    try {
                        JSONObject jsonGxStr = new JSONObject(StrGx);
                        final String MD5 = jsonGxStr.getString("APKMD5");
                        final String CRC =  jsonGxStr.getString("APKToKenCR");
                        final String SHA = jsonGxStr.getString("APKToKen");
                        final String ID = jsonGxStr.getString("ID");
                        final String sha = apkVerifyWithSHA(MainActivity.this,SHA);
                        System.out.println(sha);
                        System.out.println(SHA);
                        final String md5 = apkVerifyWithMD5(MainActivity.this,MD5);
                        System.out.println(md5);
                        System.out.println(MD5);
                        final String crc = apkVerifyWithCRC(MainActivity.this,CRC);
                        System.out.println(crc);
                        System.out.println(CRC);
                        if (ID.equals("1")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!sha.equals(SHA)){
                                        ScrollView ScrollView_Main = (ScrollView)findViewById(R.id.Home_MainLayout);
                                        ScrollView_Main.setVisibility(View.GONE);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }else if(!md5.equals(MD5)){
                                        ScrollView ScrollView_Main = (ScrollView)findViewById(R.id.Home_MainLayout);
                                        ScrollView_Main.setVisibility(View.GONE);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }else if(!crc.equals(CRC)){
                                        ScrollView ScrollView_Main = (ScrollView)findViewById(R.id.Home_MainLayout);
                                        ScrollView_Main.setVisibility(View.GONE);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                }
                            });
                        }else{
                            String fs = HttpUtils.doGet("https://api.misakaloli.com/app/bilibilias.php?type=json&edition=1.0&SHA="+sha+"&MD5="+md5+"&CRC="+crc+"lj="+LJ,cookie);
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
                    if (!GxUrl.equals("https://api.misakaloli.com/app/bilibilias.php?type=json&edition=0.9.1")) {
                    } else {
                        JSONObject jsonGxStr = null;
                        String StrPd = null;
                        try {
                            jsonGxStr = new JSONObject(StrGx);
                            StrPd = jsonGxStr.getString("edition");
                            final String StrNr = jsonGxStr.getString("gxnotice");
                            final String StrUrl = jsonGxStr.getString("url");
                            if (StrPd.equals("1.0")) {
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
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
                                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
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
    }



    public class bvUrl extends Thread {
        @Override
        public void run() {
            list = new ArrayList<String>();
            listVideo = new ArrayList<String>();
            listCode = new ArrayList<String>();
            /*修什么BUG？？？写个try罩住 【手动滑稽】
            哈哈好吧，这里实际上是我有点懒了，不想再分析空指针也就是其中有必需值为空时提示解析失败
            所以最快的方法就是罩住，我以后一定改写一下
             */
            try {
                String name = null;
                //定位数据
                //判断下这是不是个纯bv号
                if (isENChar(bvid)) {
                    //先检测av是否具备，因为av接口更为苛刻
                    if (bvid.contains("av")) {
                        //过滤掉多余的东西
                        //判断这是不是一个链接
                        if(bvid.contains("https")||bvid.contains("http")){
                            //给特殊的方法处理判断，传导这个链接进去 即为公共解析
                            public_jx(bvid);
                        }else {
                            //如果不是，就直接让接口解析
                            bvid = bvid.replaceAll("av", "");
                            name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid,cookie);
                            abvGo(name);
                        }
                    } else if(bvid.contains("bv")||bvid.contains("BV")) {
                        //有BV也有链接头
                        if(bvid.contains("https")||bvid.contains("http")){
                            //给特殊的方法处理判断，传导这个链接进去 即为公共解析
                            public_jx(bvid);
                        }else{
                            //相反没有携带则就是BV编号
                            name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid,cookie);
                            abvGo(name);
                        }
                    }else
                    {
                        //AV/BV都没有，则可能是番剧的或者是手机分享的av，bv链接
                        //给番剧接口处理即可,这里先判断是不是手机分享地址
                        if(bvid.contains("https://b23.tv/")){
                            //这里来看看是不是番剧
                            if(bvid.contains("ep")){
                                epGo(bvid);
                            }else{
                                //如果不是则一定是视频地址
                                public_jx(bvid);
                            }
                        }else {
                            //如果都不是那就应该是电脑番剧链接
                            epGo(bvid);
                        }
                    }
                } else {
                    //全数字一定是av
                    System.out.println("错误提示");
                    name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid,cookie);
                    abvGo(name);
                }
            }catch (Exception e) {
                System.out.println(e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭弹窗，提示失败
                        pd2.cancel();
                        Toast.makeText(getApplicationContext(), "看起来没有解析到-1", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    //查询按键1
    public void onBvUrl(View view) throws IOException {
        pd2 = ProgressDialog.show(MainActivity.this, "提示", "正在拉取数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText EditText1 = (EditText) findViewById(R.id.EditText1);
                bvid = EditText1.getText().toString();
                String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"token.txt";
                String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                try {
                    csrf = BilibiliPost.fileRead(csrfPath);
                    toKen = BilibiliPost.fileRead(ToKenPath);
                    cookie = BilibiliPost.fileRead(CookiePath);
                    System.out.println(toKen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl","");
                System.out.println(likeStr);
                URL = sj(likeStr,"url\":\"","\",\"");
                oauthKey = sj(likeStr,"oauthKey\":\"","\"");
                System.out.println(URL);
               String pd = BilibiliPost.nav(toKen);
               if(pd.equals("0")){
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           new bvUrl().start();
                       }
                   });
               }else{
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           init("https://api.misakaloli.com/app/bilibiliasLogin.php?URL="+URL);
                           WebView webView1 = (WebView)findViewById(R.id.WebView1);
                           webView1.setWebChromeClient(new WebChromeClient());
                           webView1.setWebViewClient(new NewWebViewClient());
                           ScrollView webLayout = (ScrollView)findViewById(R.id.WebLayout);
                           webLayout.setVisibility(View.VISIBLE);
                           pd2.cancel();
                           Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }
        }).start();

    }

    //第二个页面
    public void onBvUrl0(View view) {
        pd2 = ProgressDialog.show(MainActivity.this, "提示", "正在拉取数据");
        EditText EditText1 = (EditText) findViewById(R.id.EditText2);
        bvid = EditText1.getText().toString();
        new bvUrl().start();
    }


    //下载函数
    public class download extends Thread {
        @Override
        public void run() {
            StrData = HttpUtils.doGet(jxUrl,cookie);
            System.out.println(jxUrl);
            StrData = sj(StrData,"url\":\"","\",");
            StrData = unicodeDecode(StrData);
            RequestParams params = new RequestParams(StrData);
            //检测突破
            params.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            params.addHeader("referer"," https://www.bilibili.com/video/av"+aid+"/");
            params.addHeader("Cookie",cookie);
            params.setAutoResume(true);//设置是否在下载是自动断点续传
            params.setAutoRename(false);//设置是否根据头信息自动命名文件
            params.setSaveFilePath(getExternalFilesDir("哔哩哔哩视频").toString()+"/"+ type +aid+"."+ videoType);//设置下载地址
            params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
            params.setCancelFast(true);//是否可以被立即停止.
            //下面的回调都是在主线程中运行的,这里设置的带进度的回调
            cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onCancelled(CancelledException arg0) {
                    Log.i("tag", "取消" + Thread.currentThread().getName());
                }

                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    Log.i("tag", "onError: 失败" + Thread.currentThread().getName());
                    progressDialog.dismiss();
                }

                @Override
                public void onFinished() {
                    Log.i("tag", "完成,每次取消下载也会执行该方法" + Thread.currentThread().getName());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(File arg0) {
                    Log.i("tag", "下载成功的时候执行" + Thread.currentThread().getName());
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    if (isDownloading) {
                        progressDialog.setProgress((int) (current * 100 / total));
                        Log.i("tag", "下载中,会不断的进行回调:" + Thread.currentThread().getName());
                    }
                }

                @Override
                public void onStarted() {
                    Log.i("tag", "开始下载的时候执行" + Thread.currentThread().getName());
                    progressDialog.show();
                }

                @Override
                public void onWaiting() {
                    Log.i("tag", "等待,在onStarted方法之前执行" + Thread.currentThread().getName());
                }

            });
        }
    }

    /**
     * 下面分别对应着不同格式的按钮下载
     * @param view
     */
    public void onBvUrl1(View view) {
        type = "1080格式";
        initProgressDialog();
        new download().start();
    }


    public void goSet(View view){
        Intent intent= new Intent();
        cookie = "10086";
        intent.setClass(MainActivity.this, UserActivity.class);
        startActivity(intent);
    }

    public void goLive(View view){
        Intent intent= new Intent();
        intent.setClass(MainActivity.this,LiveActivity.class);
        MainActivity.this.startActivity(intent);
    }


    //下载进度对话框
    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle("下载文件");
        //设置信息
        progressDialog.setMessage("玩命下载中...");
        //设置空白处不消失
        progressDialog.setCancelable(false);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置按钮
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "暂停",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消正在下载的操作
                cancelable.cancel();
            }});
    }



    //取中间方法
    public  String sj(String str, String start, String end)
    {
        if (str.contains(start) && str.contains(end))
        {
            str = str.substring(str.indexOf(start) + start.length());
            return str.substring(0, str.indexOf(end));
        }
        return "";
    }

    //图片加载方法
    private Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //unicode解析
    public static String unicodeDecode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);

                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


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
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }

    //bv和av号的检测
    public boolean isENChar(String string) {
        boolean flag = false;
        Pattern p = Pattern.compile("[a-zA-z]");
        if(p.matcher(string).find()) {
            flag = true;
        }
        return flag;
    }

    //av和bv解析方法
    private void abvGo(String name) throws JSONException {
        ImageUrl = sj(name, "pic\":\"", "\",");
        Title = sj(name, "title\":\"", "\",");
        aid = sj(name, "\"aid\":", ",\"");
        bvid = sj(name, "\"bvid\":\"", "\",");
        up = sj(name, "\"name\":\"", "\",");
        System.out.println(aid);

        JSONObject json = new JSONObject(name);
        JSONObject data = json.getJSONObject("data");
        JSONArray pages = data.getJSONArray("pages");
        System.out.println(pages);
        name = sj(name, "cid\":", ",\"");
        //初步获取视频分辨率
        jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json&fourk=1";
        String jsonStr = HttpUtils.doGet(jxUrl,cookie);
        System.out.println(jsonStr);
        //分辨率解析列表展示
        JSONObject jsonVideo = new JSONObject(jsonStr);
        JSONObject dataStr = jsonVideo.getJSONObject("data");
        JSONArray pagesVideo = dataStr.getJSONArray("accept_description");
        for(int i=0;i<pagesVideo.length();i++)
        {
            listVideo.add(pagesVideo.getString(i));
        }

        if(pages.length()==1){
            list.add(name);
            System.out.println(ImageUrl);
            if (name.equals("") || bvid.equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭弹窗，提示失败
                        pd2.cancel();
                        Toast.makeText(getApplicationContext(), "看起来没有解析到", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //控件定位
                LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
                ScrollView1 = (ScrollView) findViewById(R.id.ScrollView1);
                ImageView1 = (ImageView) findViewById(R.id.ImageView1);
                TextView1 = (TextView) findViewById(R.id.TextView1);
                TextView2 = (TextView) findViewById(R.id.UP);

                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        TextView2.setText(up);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);
                        ImageView1.setImageBitmap(bitmap);
                        LinearLayout1.setVisibility(View.GONE);
                        ScrollView1.setVisibility(View.VISIBLE);
                    }
                });
                pd2.cancel();
            }
        }else{
            for(int i=0;i<pages.length();i++)
            {
                JSONObject honor = pages.getJSONObject(i);
                String cid1 = honor.getString("cid");
                list.add(cid1);
            }
            if (name.equals("") || bvid.equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭弹窗，提示失败
                        pd2.cancel();
                        Toast.makeText(getApplicationContext(), "看起来没有解析到", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //控件定位
                LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
                ScrollView1 = (ScrollView) findViewById(R.id.ScrollView1);
                ImageView1 = (ImageView) findViewById(R.id.ImageView1);
                TextView1 = (TextView) findViewById(R.id.TextView1);
                TextView2 = (TextView) findViewById(R.id.UP);

                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        TextView2.setText(up);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);
                        ImageView1.setImageBitmap(bitmap);
                        LinearLayout1.setVisibility(View.GONE);
                        ScrollView1.setVisibility(View.VISIBLE);
                    }
                });
                pd2.cancel();
            }
        }
    }

    //番剧单独解析方法
    private void epGo(String name) throws JSONException {
        //这里是判断一下番剧的数据
        //获取番剧页面源码
        String ep = HttpUtils.doGet(name,cookie);
        //截取需要的部分
        String epStr = sj(ep, "<script>window.__INITIAL_STATE__=", "</script>");
        Title = sj(epStr, "\"h1Title\":\"", "\",");
        //再单独切出aid，bvid，图片链接这些东西
        epStr = sj(epStr, "epInfo", "parentNode.removeChild");
        System.out.println(epStr);
        aid = sj(epStr, "aid\":", ",\"");
        bvid = sj(epStr, "bvid\":\"BV", "\",");
        ImageUrl = sj(epStr, "bangumi\",\"cover\":\"", "\",");
        //图片需要转码
        ImageUrl = "http:" + unicodeDecode(ImageUrl);
        //番剧列表化
        String epJson = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + aid,cookie);
        JSONObject json = new JSONObject(epJson);
        JSONObject data = json.getJSONObject("data");
        JSONArray pages = data.getJSONArray("pages");
        System.out.println(pages);
        //截取标题 截取cid
        name = sj(epStr, "cid\":", ",\"");
        cid = name;
        //初步获取视频分辨率
        jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json&fourk=1";
        String jsonStr = HttpUtils.doGet(jxUrl,cookie);
        System.out.println(jsonStr);
        //分辨率解析列表展示
        JSONObject jsonVideo = new JSONObject(jsonStr);
        JSONObject dataStr = jsonVideo.getJSONObject("data");
        JSONArray pagesVideo = dataStr.getJSONArray("accept_description");
        for(int i=0;i<pagesVideo.length();i++)
        {
            listVideo.add(pagesVideo.getString(i));
        }
        //判断下这个截取的数据是不是空的
        if(pages.length()==1) {
            list.add(name);
            if (bvid.equals("") || aid.equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭弹窗，提示失败
                        pd2.cancel();
                        Toast.makeText(getApplicationContext(), "看起来没有解析到", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //定位组件
                LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
                ScrollView1 = (ScrollView) findViewById(R.id.ScrollView1);
                ImageView1 = (ImageView) findViewById(R.id.ImageView1);
                TextView1 = (TextView) findViewById(R.id.TextView1);
                TextView2 = (TextView) findViewById(R.id.UP);
                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        ImageView1.setImageBitmap(bitmap);
                        LinearLayout1.setVisibility(View.GONE);
                        ScrollView1.setVisibility(View.VISIBLE);
                        TextView2.setVisibility(View.GONE);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);
                    }
                });
                pd2.cancel();
            }
        }else{
            for(int i=0;i<pages.length();i++)
            {
                JSONObject honor = pages.getJSONObject(i);
                String cid1 = honor.getString("cid");
                list.add(cid1);
            }
            if (bvid.equals("") || aid.equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭弹窗，提示失败
                        pd2.cancel();
                        Toast.makeText(getApplicationContext(), "看起来没有解析到", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //定位组件
                LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
                ScrollView1 = (ScrollView) findViewById(R.id.ScrollView1);
                ImageView1 = (ImageView) findViewById(R.id.ImageView1);
                TextView1 = (TextView) findViewById(R.id.TextView1);
                TextView2 = (TextView) findViewById(R.id.UP);
                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        ImageView1.setImageBitmap(bitmap);
                        LinearLayout1.setVisibility(View.GONE);
                        ScrollView1.setVisibility(View.VISIBLE);
                        TextView2.setVisibility(View.GONE);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);
                    }
                });
                pd2.cancel();
            }
        }
    }

    private void public_jx(String name) throws JSONException {
        name = HttpUtils.doGet(name,cookie);
        bvid = sj(name,"href=\"https://www.bilibili.com/video/av","/\">");
        name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid,cookie);
        abvGo(name);
    }


    //下面是一些对视频点赞/投币等操作

    //点赞
    public void  goLike(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pd = BilibiliPost.Like(bvid,toKen,csrf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pd.equals("0")){
                            Toast.makeText(getApplicationContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        }else if(pd.equals("65006")){
                            Toast.makeText(getApplicationContext(), "我的妈天瓜子，不能重复点赞的", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "点赞失败了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    //投币方法
    public void  GoAdd(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pd = BilibiliPost.add(bvid,"1",toKen,csrf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pd.equals("0")){
                            Toast.makeText(getApplicationContext(), "投币成功", Toast.LENGTH_SHORT).show();
                        }else if(pd.equals("-104")){
                            Toast.makeText(getApplicationContext(), "出大问题，大佬你似乎没硬币了，这下惨了", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "投币失败了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    //三连方法
    public void  GoTriple(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pd = BilibiliPost.triple(bvid,toKen,csrf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pd.equals("0")){
                            Toast.makeText(getApplicationContext(), "三连成功感谢推荐", Toast.LENGTH_SHORT).show();
                        }else if(pd.equals("-101")){
                            Toast.makeText(getApplicationContext(), "出大问题，登录出问题了", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "三连失败了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void init(String LoginUrl){
        WebView webView1 = (WebView)findViewById(R.id.WebView1);
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
                System.out.println(likeStr);
                URL = sj(likeStr,"url\":\"","\",\"");
                oauthKey = sj(likeStr,"oauthKey\":\"","\"");
                System.out.println(URL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init("https://api.misakaloli.com/app/bilibiliasLogin.php?URL="+URL);
                        WebView webView1 = (WebView)findViewById(R.id.WebView1);
                        webView1.setWebChromeClient(new WebChromeClient());
                        webView1.setWebViewClient(new NewWebViewClient());
                        ScrollView webLayout = (ScrollView)findViewById(R.id.WebLayout);
                        webLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    public void  UpLogin(View view) {
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
                            ScrollView webLayout = (ScrollView)findViewById(R.id.WebLayout);
                            webLayout.setVisibility(View.GONE);
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

    public void  UpLoginNew(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl","");
                System.out.println(likeStr);
                URL = sj(likeStr,"url\":\"","\",\"");
                oauthKey = sj(likeStr,"oauthKey\":\"","\"");
                System.out.println(URL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                        init("https://api.misakaloli.com/app/bilibiliasLogin.php?URL="+URL);
                        WebView webView1 = (WebView)findViewById(R.id.WebView1);
                        webView1.setWebChromeClient(new WebChromeClient());
                        webView1.setWebViewClient(new NewWebViewClient());
                        ScrollView webLayout = (ScrollView)findViewById(R.id.WebLayout);
                        webLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
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

    public void ListArray(List<String> arrayStrings){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        final Spinner spinner = findViewById(R.id.Main_Spinner1);
        spinner.setAdapter(adapter);
        //给Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String)spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                cid = data;
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void ListArrayVideo(List<String> arrayStrings){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        final Spinner spinner = findViewById(R.id.Main_Spinner2);
        spinner.setAdapter(adapter);
        //给Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String)spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                if(data.equals("高清 1080P+")){
                    type = "高清 1080P+";
                    qn = "112";
                }else if(data.equals("高清 1080P")){
                    type = "高清 1080P";
                    qn = "80";
                }else if(data.equals("高清 720P")){
                    type = "高清 720P";
                    qn = "64";
                }else if(data.equals("清晰 480P")){
                    type = "清晰 480P";
                    qn = "32";
                }else if(data.equals("流畅 360P")){
                    type = "流畅 360P";
                    qn = "16";
                }else if(data.equals("高清 1080P60")){
                    type = "高清 1080P60";
                    qn = "116";
                }else if(data.equals("高清 720P60")){
                    type = "高清 720P60";
                    qn = "74";
                }else if(data.equals("超清 4K")){
                    type = "超清 4K";
                    qn = "120";
                }
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void ListArrayCode(List<String> arrayStrings){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        final Spinner spinner = findViewById(R.id.Main_Spinner3);
        spinner.setAdapter(adapter);
        //给Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String)spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                fnval = data ;
                if (fnval.equals("flv")){
                    videoType = "flv";
                    fnval = "0";
                }else if(fnval.equals("mp4")){
                    videoType = "mp4";
                    fnval = "1";
                }else{
                    videoType = "flv";
                    fnval = "2";
                }
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    //底层程序加固


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







