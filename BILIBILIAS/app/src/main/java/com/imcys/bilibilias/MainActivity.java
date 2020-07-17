package com.imcys.bilibilias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


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
    private ImageView ImageView1;
    private TextView TextView1;
    private Callback.Cancelable cancelable;
    private String StrData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);//绑定注解

        //检测动态权限
        checkPermission();

        //更新检测
        new Thread(new Runnable() {
            @Override
            public void run() {
                String StrGx = HttpUtils.doGet("https://ycx.mxjs.xyz/sqv8/app/bilibilias.php");
                if (StrGx.equals("")) {
                }
                else {
                    String StrPd = sj(StrGx, "『", "』");
                    final String StrNr = sj(StrGx, "《", "》");
                    final String StrUrl = sj(StrGx, "【", "】");
                    if (StrPd.equals("0.2")) {
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
                                dialog.show();
                            }
                        });
                    }
                }
            }
        }).start();
    }


    public class bvUrl extends Thread {
        @Override
        public void run() {
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
                        bvid = bvid.replaceAll("av", "");
                        name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid);
                    } else {
                        name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid);
                    }
                } else {
                    name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid);
                }
                ImageUrl = sj(name, "pic\":\"", "\",");
                Title = sj(name, "title\":\"", "\",");
                aid = sj(name, "\"aid\":", ",\"");
                bvid = sj(name, "\"bvid\":\"", "\",");
                System.out.println(aid);
                name = sj(name, "cid\":", ",\"");
                System.out.println(ImageUrl);
                //判断传入参数
                if (name.equals("") || bvid.equals("")) {
                    //这里是判断一下番剧的数据
                    //获取番剧页面源码
                    String ep = HttpUtils.doGet(bvid);
                    //截取需要的部分
                    String epStr = sj(ep, "<script>window.__INITIAL_STATE__=", "</script>");
                    ;
                    Title = sj(epStr, "\"h1Title\":\"", "\",");
                    //再单独切出aid，bvid，图片链接这些东西
                    epStr = sj(epStr, "epInfo", "parentNode.removeChild");
                    System.out.println(epStr);
                    aid = sj(epStr, "aid\":", ",\"");
                    bvid = sj(epStr, "bvid\":\"BV", "\",");
                    ImageUrl = sj(epStr, "bangumi\",\"cover\":\"", "\",");
                    //图片需要转码
                    ImageUrl = "http:" + unicodeDecode(ImageUrl);
                    //截取标题 截取cid
                    name = sj(epStr, "cid\":", ",\"");
                    //判断下这个截取的数据是不是空的
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
                            }
                        });
                        //输出视频下载地址
                        jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json";
                        pd2.cancel();
                    }
                } else {
                    //控件定位
                    LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
                    ScrollView1 = (ScrollView) findViewById(R.id.ScrollView1);
                    ImageView1 = (ImageView) findViewById(R.id.ImageView1);
                    TextView1 = (TextView) findViewById(R.id.TextView1);

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
                        }
                    });
                    jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json";
                    pd2.cancel();
                }
            }catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭弹窗，提示失败
                        pd2.cancel();
                        Toast.makeText(getApplicationContext(), "看起来没有解析到", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    //查询按键1
    public void onBvUrl(View view) {
        pd2 = ProgressDialog.show(MainActivity.this, "提示", "正在拉取数据");
        EditText EditText1 = (EditText) findViewById(R.id.EditText1);
        bvid = EditText1.getText().toString();
        new bvUrl().start();
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
            StrData = HttpUtils.doGet(jxUrl);
            System.out.println(jxUrl);
            StrData = sj(StrData,"url\":\"","\",");
            StrData = unicodeDecode(StrData);
            RequestParams params = new RequestParams(StrData);
            //检测突破
            params.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            params.addHeader("referer"," https://www.bilibili.com/video/av"+aid+"/");
            params.setAutoResume(true);//设置是否在下载是自动断点续传
            params.setAutoRename(false);//设置是否根据头信息自动命名文件
            params.setSaveFilePath("/storage/emulated/0/Android/data/com.imcys.bilibilias/哔哩哔哩封面/"+ type +aid+".flv");
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
        jxUrl = jxUrl + "&qn=80";
        initProgressDialog();
        new download().start();
    }

    public void onBvUrl2(View view) {
        type = "720格式";
        jxUrl = jxUrl + "&qn=64";
        initProgressDialog();
        new download().start();
    }

    public void onBvUrl3(View view) {
        type = "460格式";
        jxUrl = jxUrl + "&qn=32";
        initProgressDialog();
        new download().start();
    }

    public void onBvUrl4(View view) {
        type = "320格式";
        jxUrl = jxUrl + "&qn=16";
        initProgressDialog();
        new download().start();
    }

    public void goSet(View view){
        Intent intent= new Intent();
        intent.setClass(MainActivity.this,SetActivity.class);
        startActivity(intent);
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
}





