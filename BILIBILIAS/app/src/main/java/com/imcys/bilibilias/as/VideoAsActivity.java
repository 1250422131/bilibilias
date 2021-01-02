package com.imcys.bilibilias.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static java.lang.Thread.sleep;


public class VideoAsActivity extends AppCompatActivity {

    private String DLPath;
    private Intent intent ;
    private ProgressDialog pd2;
    private ProgressDialog progressDialog;
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
    private String mid;
    private JzvdStd jzVideoPlayerStandard;
    private String VideoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_dl);
        intent = getIntent();
        csrf = intent.getStringExtra("csrf");
        toKen = intent.getStringExtra("toKen");
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");

        //BUGly
        CrashReport.initCrashReport(getApplicationContext(), "1bb190bc7d", false);

        jzVideoPlayerStandard = (JzvdStd)findViewById(R.id.As_VideoPlayer);
        Glide.with(this)
                .load("https://s3.ax1x.com/2020/12/13/rmtGX6.jpg")
                .into(jzVideoPlayerStandard.posterImageView);


        Intent intent =getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        //设置接收类型为文本
        if (Intent.ACTION_SEND.equals(action) && type != null){
            //来自APP/B站分享解析
            if ("text/plain".equals(type)) {
                //获取分享数据
                pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
                String data = intent.getStringExtra(Intent.EXTRA_TEXT)+"|";
                System.out.println(data);
                bvid = "https:"+sj(data,"https:","|");
                String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"token.txt";
                String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                try {
                    csrf = BilibiliPost.fileRead(csrfPath);
                    toKen = BilibiliPost.fileRead(ToKenPath);
                    cookie = BilibiliPost.fileRead(CookiePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new bvUrl().start();
            }
        }else if (null != intent && null != intent.getData()) {
            //来着网页进行解析
            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
            // uri 就相当于 web 页面中的链接
            Uri uri = intent.getData();
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
           // System.out.println("scheme=" + scheme + ",host=" + host+ ",port=" + port + ",path=" + path+ ",query=" + uri.getQuery()+ ",key1=" + key1 + "，key2=" + key2);
            bvid = uri.getQueryParameter("video");
            String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"token.txt";
            String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
            String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
            try {
                bvid = uri.getQueryParameter("video");
                csrf = BilibiliPost.fileRead(csrfPath);
                toKen = BilibiliPost.fileRead(ToKenPath);
                cookie = BilibiliPost.fileRead(CookiePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new bvUrl().start();
        }
        UserVideoAs();
    }

    //播放器
    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }


    //这里是个人主页传回数据解析
    private void UserVideoAs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String UserVideoAid = intent.getStringExtra("UserVideoAid");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(UserVideoAid != null){
                            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
                            bvid = UserVideoAid;
                            String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"token.txt";
                            String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                            String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                            try {
                                csrf = BilibiliPost.fileRead(csrfPath);
                                toKen = BilibiliPost.fileRead(ToKenPath);
                                cookie = BilibiliPost.fileRead(CookiePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            new bvUrl().start();
                        }
                    }
                });
            }
        }).start();
    }


    //下面来做解析
    public class bvUrl extends Thread {
        @Override
        public void run() {
            //这里先New几个视频参数列表
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
                    }else if(bvid.contains("ep")||bvid.contains("EP"))
                    {
                        //分解掉直接输入ep和带链接的
                        if (!bvid.contains("https") && !bvid.contains("http")) {
                            bvid = "https://www.bilibili.com/bangumi/play/" + bvid;
                        }
                        epGo(bvid);
                    }else if(bvid.contains("ss")||bvid.contains("SS")){
                        //输入一个ss的番剧链接，为了准确
                        if (!bvid.contains("https") && !bvid.contains("http")) {
                            bvid = "https://www.bilibili.com/bangumi/play/" + bvid;
                        }
                        public_jx(bvid);
                    }else if(bvid.contains("bilibili.com")){
                        //输入一个视频链接，直接给截取公共解析
                        //这个方案存在问题，当输入错误链接异常时可能无法及时停止解析
                        public_jx(bvid);
                    } else {
                        //AV/BV/ep/ss/BiliBili都没有，则可能是番剧的或者是手机分享的av，bv链接
                        //判断下是不是分享链接现在这里直接解析
                        if(bvid.contains("https://b23.tv/")){
                            public_jx(bvid);
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //关闭弹窗，提示失败
                                    pd2.cancel();
                                    Toast.makeText(getApplicationContext(), "看起来没有解析到-2", Toast.LENGTH_SHORT).show();
                                }
                            });
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
        pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText EditText1 = (EditText) findViewById(R.id.As_EditText1);
                bvid = EditText1.getText().toString();
                //伪装请求用户页面
                System.out.println(URL);
                String pd = BilibiliPost.nav(toKen);
                //判断登录是否正常
                if(pd.equals("0")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new bvUrl().start();
                        }
                    });
                }
            }
        }).start();
    }

    public void barrageGet(View view){
        //下载弹幕
        pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在获取弹幕");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取下载路径
                    String DMUrl = "http://api.bilibili.com/x/v1/dm/list.so?oid="+cid;
                    Connection.Response response = Jsoup.connect(DMUrl)
                            .method(Connection.Method.GET)
                            .timeout(5000)
                            .execute();
                    String DMXml = response.body().replace("</d>","</d>\n");
                    DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/Path.txt");
                    DLPath = DLPath+Title+type+cid+".xml";
                    BilibiliPost.fileWrite(DLPath,DMXml);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd2.cancel();
                            Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    //封面下载
    public void FMDownload(View view){
        if(bvid != null){
            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String FMJsonStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/view?bvid=" + bvid, cookie);
                    String FMUrl = null;
                    try {
                        JSONObject FMJson = new JSONObject(FMJsonStr);
                        FMJson = FMJson.getJSONObject("data");
                        FMUrl = FMJson.getString("pic");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Bitmap FMBitmap = BilibiliPost.returnBitMap(FMUrl);
                    savePhoto(VideoAsActivity.this, FMBitmap);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "下载完成,已经通知相册更新", Toast.LENGTH_SHORT).show();
                            pd2.cancel();
                        }
                    });
                }
            }).start();
        }else {
            Toast.makeText(getApplicationContext(), "请先解析", Toast.LENGTH_SHORT).show();
        }
    }


    //图片保存方案
    //保存到本地
    public void savePhoto(Context context, Bitmap bitmap){
        File photoDir = new File(Environment.getExternalStorageDirectory(),"DCIM/BILIBILIAS");
        if (!photoDir.exists()){
            photoDir.mkdirs();
        }
        String fileName = System.currentTimeMillis()+".jpg";
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
        VideoJson = name;
        ImageUrl = sj(name, "pic\":\"", "\",");
        Title = sj(name, "title\":\"", "\",");
        aid = sj(name, "\"aid\":", ",\"");
        bvid = sj(name, "\"bvid\":\"", "\",");
        up = sj(name, "\"name\":\"", "\",");
        System.out.println(aid);
        JSONObject json = new JSONObject(name);
        JSONObject data = json.getJSONObject("data");
        JSONArray pages = data.getJSONArray("pages");
        JSONObject UPNameJson = data.getJSONObject("owner");
        final String UPUser = UPNameJson.getString("name");
        String VideoDesc = data.getString("desc");
        System.out.println(pages);
        name = sj(name, "cid\":", ",\"");
        String VideoTitle = null;
        //初步获取视频分辨率
        jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json&fourk=1";
        String jsonStr = HttpUtils.doGet(jxUrl,cookie);
        System.out.println(jsonStr);
        //分辨率解析列表展示
        JSONObject jsonVideo = new JSONObject(jsonStr);
        JSONObject dataStr = jsonVideo.getJSONObject("data");
        JSONArray pagesVideo = dataStr.getJSONArray("accept_description");
        JSONArray quality = dataStr.getJSONArray("accept_quality");
        for(int i=0;i<pagesVideo.length();i++)
        {
            String hz = quality.getString(i);
            listVideo.add(pagesVideo.getString(i)+"["+hz+"]");
        }
        if(pages.length()==1){
            VideoTitle =pages.getString(0);
            JSONObject TitleJson =  new JSONObject(VideoTitle);
            String TitleStr = TitleJson.getString("part");
            list.add("["+name+"]"+TitleStr);
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
                //定位组件
                ImageView1 = (ImageView) findViewById(R.id.As_ImageView);
                TextView1 = (TextView) findViewById(R.id.As_Title);
                TextView2 = (TextView) findViewById(R.id.As_UP);
                final Bitmap bitmap = returnBitMap(ImageUrl);
                StrData = HttpUtils.doGet(jxUrl+"&fnval=1",cookie);
                StrData = sj(StrData,"url\":\"","\",");
                StrData = unicodeDecode(StrData);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        TextView2.setText(UPUser);
                        ImageView1.setImageBitmap(bitmap);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);

                        JZDataSource jzDataSource = new JZDataSource(StrData,Title);
                        jzDataSource.headerMap.put("Cookie",cookie);
                        jzDataSource.headerMap.put("Referer","https://www.bilibili.com/video/av"+aid+"/");
                        jzDataSource.headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
                        jzVideoPlayerStandard = (JzvdStd)findViewById(R.id.As_VideoPlayer);
                        jzVideoPlayerStandard.setUp(jzDataSource,JzvdStd.SCREEN_NORMAL);
                        Glide.with(VideoAsActivity.this).load(ImageUrl).into(jzVideoPlayerStandard.posterImageView);

                    }
                });
                pd2.cancel();
            }
        }else{
            for(int i=0;i<pages.length();i++)
            {
                JSONObject honor = pages.getJSONObject(i);
                String TitleStr = honor.getString("part");
                String cid1 = honor.getString("cid");
                list.add("["+cid1+"]"+TitleStr);
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
                ImageView1 = (ImageView) findViewById(R.id.As_ImageView);
                TextView1 = (TextView) findViewById(R.id.As_Title);
                TextView2 = (TextView) findViewById(R.id.As_UP);
                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                StrData = HttpUtils.doGet(jxUrl+"&fnval=1",cookie);
                System.out.println(jxUrl+"&fnval=1");
                StrData = sj(StrData,"url\":\"","\",");
                StrData = unicodeDecode(StrData);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        TextView2.setText(UPUser);
                        ImageView1.setImageBitmap(bitmap);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);


                        JZDataSource jzDataSource = new JZDataSource(StrData,Title);
                        jzDataSource.headerMap.put("Cookie",cookie);
                        jzDataSource.headerMap.put("Referer","https://www.bilibili.com/video/av"+aid+"/");
                        jzDataSource.headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
                        jzVideoPlayerStandard = (JzvdStd)findViewById(R.id.As_VideoPlayer);
                        jzVideoPlayerStandard.setUp(jzDataSource,JzvdStd.SCREEN_NORMAL);
                        Glide.with(VideoAsActivity.this).load(ImageUrl).into(jzVideoPlayerStandard.posterImageView);

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
        VideoJson = epJson;
        JSONObject json = new JSONObject(epJson);
        JSONObject data = json.getJSONObject("data");
        JSONObject UPNameJson = data.getJSONObject("owner");
        final String UPUser = UPNameJson.getString("name");
        String VideoDesc = data.getString("desc");
        JSONArray pages = data.getJSONArray("pages");
        String VideoTitle = null;
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
        JSONArray quality = dataStr.getJSONArray("accept_quality");
        for(int i=0;i<pagesVideo.length();i++)
        {
            String hz = quality.getString(i);
            listVideo.add(pagesVideo.getString(i)+"["+hz+"]");
        }
        //判断下这个截取的数据是不是空的
        if(pages.length()==1) {
            VideoTitle = pages.getString(0);
            JSONObject TitleJson =  new JSONObject(VideoTitle);
            String TitleStr = TitleJson.getString("part");
            list.add("["+name+"]"+TitleStr);
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
                //定位组件
                ImageView1 = (ImageView) findViewById(R.id.As_ImageView);
                TextView1 = (TextView) findViewById(R.id.As_Title);
                TextView2 = (TextView) findViewById(R.id.As_UP);
                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                StrData = HttpUtils.doGet(jxUrl+"&fnval=1",cookie);
                System.out.println(jxUrl);
                StrData = sj(StrData,"url\":\"","\",");
                StrData = unicodeDecode(StrData);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        TextView2.setText(UPUser);
                        ImageView1.setImageBitmap(bitmap);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);

                        JZDataSource jzDataSource = new JZDataSource(StrData,Title);
                        jzDataSource.headerMap.put("Cookie",cookie);
                        jzDataSource.headerMap.put("Referer","https://www.bilibili.com/video/av"+aid+"/");
                        jzDataSource.headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
                        jzVideoPlayerStandard = (JzvdStd)findViewById(R.id.As_VideoPlayer);
                        jzVideoPlayerStandard.setUp(jzDataSource,JzvdStd.SCREEN_NORMAL);
                        Glide.with(VideoAsActivity.this).load(ImageUrl).into(jzVideoPlayerStandard.posterImageView);

                    }
                });
                pd2.cancel();
            }
        }else{
            for(int i=0;i<pages.length();i++)
            {
                JSONObject honor = pages.getJSONObject(i);
                String TitleStr = honor.getString("part");
                String cid1 = honor.getString("cid");
                list.add("["+cid1+"]"+TitleStr);
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
                ImageView1 = (ImageView) findViewById(R.id.As_ImageView);
                TextView1 = (TextView) findViewById(R.id.As_Title);
                TextView2 = (TextView) findViewById(R.id.As_UP);
                final Bitmap bitmap = returnBitMap(ImageUrl);
                //显示番剧图片
                StrData = HttpUtils.doGet(jxUrl+"&fnval=1",cookie);
                System.out.println(jxUrl);
                StrData = sj(StrData,"url\":\"","\",");
                StrData = unicodeDecode(StrData);
                //显示番剧图片
                ImageView1.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        TextView1.setText(Title);
                        TextView2.setText(UPUser);
                        ImageView1.setImageBitmap(bitmap);
                        ListArray(list);
                        ListArrayVideo(listVideo);
                        listCode.add("flv");
                        listCode.add("mp4");
                        ListArrayCode(listCode);

                        JZDataSource jzDataSource = new JZDataSource(StrData,Title);
                        jzDataSource.headerMap.put("Cookie",cookie);
                        jzDataSource.headerMap.put("Referer","https://www.bilibili.com/video/av"+aid+"/");
                        jzDataSource.headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
                        jzVideoPlayerStandard = (JzvdStd)findViewById(R.id.As_VideoPlayer);
                        jzVideoPlayerStandard.setUp(jzDataSource,JzvdStd.SCREEN_NORMAL);
                        Glide.with(VideoAsActivity.this).load(ImageUrl).into(jzVideoPlayerStandard.posterImageView);
                    }
                });
                pd2.cancel();
            }
        }
    }

    private void public_jx(String name) throws JSONException {
        name = HttpUtils.doGet(name,cookie);
        name = sj(name, "<script>window.__INITIAL_STATE__=", "</script>");
        bvid = sj(name,"aid\":",",");
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


    //视频下载弹窗
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
            params.addHeader("referer","https://www.bilibili.com/video/av"+aid+"/");
            params.addHeader("Cookie",cookie);
            //获取断点配置
            try {
                DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/断点续传设置.txt");
                if (DLPath.equals("1")){
                    params.setAutoResume(true);//设置是否在下载是自动断点续传
                }else{
                    params.setAutoResume(false);//设置是否在下载是自动断点续传
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            params.setAutoRename(false);//设置是否根据头信息自动命名文件
            try {
                //获取下载路径
                DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/Path.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            DLPath = DLPath+Title+type+cid+"."+ videoType;
            params.setSaveFilePath(DLPath);//设置下载地址
            params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
            params.setCancelFast(true);//是否可以被立即停止.
            //下面的回调都是在主线程中运行的,这里设置的带进度的回调
            cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onCancelled(CancelledException arg0) {
                    Log.i("tag", "取消" + Thread.currentThread().getName());
                    Toast.makeText(getApplicationContext(), "取消了下载", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    Log.i("tag", "onError: 失败" + Thread.currentThread().getName());
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFinished() {
                    Log.i("tag", "完成,每次取消下载也会执行该方法" + Thread.currentThread().getName());
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(File arg0) {
                    Log.i("tag", "下载成功的时候执行" + Thread.currentThread().getName());
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    if (isDownloading) {

                        double videoFileMb = (double) (total / 1048576);
                        double videoFileMbDl = (double) (current / 1048576);
                        progressDialog.setMessage("视频大小"+videoFileMb+"MB\n"+"当前下载"+videoFileMbDl+"MB\n"+"当前下载地址\n"+DLPath+"\n进入设置页面设置下载地址");
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
        if(bvid!=null){
            type = "1080格式";
            initProgressDialog();
            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(VideoJson);
                        JSONObject dlJsonStr = new JSONObject(VideoJson);
                        dlJsonStr = dlJsonStr.getJSONObject("data");
                        dlJsonStr = dlJsonStr.getJSONObject("rights");
                        String pdVideo = dlJsonStr.getString("no_reprint");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd2.cancel();
                                if(pdVideo.equals("1")){
                                    AlertDialog aldg;
                                    AlertDialog.Builder adBd=new AlertDialog.Builder(VideoAsActivity.this);
                                    adBd.setTitle("警告");
                                    adBd.setMessage("本视频UP主声明了，“未经作者授权，禁止转载”，请注意哦。\n违反规定造成一切后果自行承担。\n是否继续下载？？");
                                    adBd.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new download().start();
                                        }
                                    });
                                    adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    aldg=adBd.create();
                                    aldg.show();
                                }else{
                                    new download().start();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            Toast.makeText(getApplicationContext(), "你需要输入点东西", Toast.LENGTH_SHORT).show();
        }
    }

    //下载进度对话框
    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle("下载文件");
        //获取信息
        try {
            //获取下载路径
            DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/Path.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DLPath = DLPath+Title+type+cid+"."+ videoType;
        progressDialog.setMessage("当前下载地址\n"+DLPath+"\n进入设置页面设置下载地址");
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




    //视频格式设置-->下拉菜单方案
    public void ListArray(List<String> arrayStrings){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        final Spinner spinner = findViewById(R.id.As_Main_Spinner1);
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
                cid = sj(data,"[","]");
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
        final Spinner spinner = findViewById(R.id.As_Main_Spinner2);
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
                Toast.makeText(VideoAsActivity.this, data, Toast.LENGTH_SHORT).show();
                type = data;
                qn = sj(data,"[","]");
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
        final Spinner spinner = findViewById(R.id.As_Main_Spinner3);
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
        java.net.URL myFileUrl = null;
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


    //unicode解析 网上抄的
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


}