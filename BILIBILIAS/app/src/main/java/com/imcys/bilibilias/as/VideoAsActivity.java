package com.imcys.bilibilias.as;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.imcys.bilibilias.AppFilePathUtils;
import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.SetActivity;
import com.imcys.bilibilias.as.video.AsPagerAdapter;
import com.imcys.bilibilias.as.video.JZPlay;
import com.imcys.bilibilias.as.video.VideoComment;
import com.imcys.bilibilias.as.video.VideoRecommend;
import com.imcys.bilibilias.fileUriUtils;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.home.VerificationUtils;

import com.imcys.bilibilias.user.UserActivity;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;


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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.regex.Pattern;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;

import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;

import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.ui.widget.DanmakuView;


import static org.xutils.common.util.DensityUtil.dip2px;


public class VideoAsActivity extends AppCompatActivity {


    private static Context context;
    private List<Reply> replyList = new ArrayList<>();
    private DanmakuView danmakuView;
    private String DLPath;
    private Intent intent;
    private ProgressDialog pd2;
    private ProgressDialog progressDialog;
    private String ImageUrl;
    private String type;
    private String Title;
    public static String bvid;
    private String jxUrl;
    public static String aid;
    private String up;
    private String upName;
    private String tName;
    public String Mid;
    private String Copyright;
    private String DownloadMethod;
    private boolean dashState = false;
    private String[] dashUrlList = {"a", "b"};
    private String dashAudioPath;
    private String dashVideoPath;
    private int dashCount = 0;
    private boolean batchIF = false;
    private int batchFor = 1;

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
    private List<String> listVideoName = new ArrayList<String>();
    private List<String> listBatch = new ArrayList<String>();
    private Spinner mProSpinner = null;
    private String cid;
    private String qn;
    private String fnval;
    private String videoType;
    public String mid;
    private JZPlay jzVideoPlayerStandard;
    private String VideoJson;
    private BaseDanmakuParser mParser;//解析器对象
    private IDanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private MessageDialog mMessageDialog;
    private String upFace;
    private int playVolume;
    private int barrageVolume;
    private int upMid = 0;

    //导入视频参数
    private int VideoId;
    private String displayDesc;
    private String channelId;
    private NotificationCompat.Builder notification;
    private NotificationManagerCompat notificationManager;
    private Uri SAF_Video;
    private Uri SAF_Index;
    private Uri SAF_Entry;
    private Uri SAF_Audio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_dl);

        context = getApplicationContext();
        intent = getIntent();
        csrf = intent.getStringExtra("csrf");
        toKen = intent.getStringExtra("toKen");
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");
        //获取下载方式 后台/前台
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DownloadMethod = sharedPreferences.getString("DownloadMethod", "1");
        //setDownloadMethod

        //饺子播放器
        jzVideoPlayerStandard = findViewById(R.id.As_VideoPlayer);


        Glide.with(this)
                .load("https://s3.ax1x.com/2020/12/13/rmtGX6.jpg")
                .into(jzVideoPlayerStandard.posterImageView);

        //弹幕控件实例化
        danmakuView = (DanmakuView) findViewById(R.id.As_DanmakuView);
        //控件着色
        //ImageView imageView = findViewById(R.id.As_search);
        //Drawable drawable = imageView.getDrawable();
        //Drawable wrap = DrawableCompat.wrap(drawable);
        //DrawableCompat.setTint(wrap, getResources().getColor(R.color.colorAccent));

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        //设置接收类型为文本
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            //来自APP/B站分享解析
            if ("text/plain".equals(type)) {
                //获取分享数据
                pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
                String data = intent.getStringExtra(Intent.EXTRA_TEXT) + "|";
                System.out.println(data);
                bvid = "https:" + sj(data, "https:", "|");
                String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "token.txt";
                String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "csrf.txt";
                String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "cookie.txt";
                try {
                    csrf = BilibiliPost.fileRead(csrfPath);
                    toKen = BilibiliPost.fileRead(ToKenPath);
                    cookie = BilibiliPost.fileRead(CookiePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new bvUrl().start();
            }
        } else if (null != intent.getData()) {
            //来着网页进行解析
            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
            // uri 就相当于 web 页面中的链接
            Uri uri = intent.getData();
            List<String> UrlPathList = uri.getPathSegments();
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();

            // System.out.println("scheme=" + scheme + ",host=" + host+ ",port=" + port + ",path=" + path+ ",query=" + uri.getQuery()+ ",key1=" + key1 + "，key2=" + key2);
            //拦截B站数据获取
            if (host.equals("m.bilibili.com")) {
                bvid = UrlPathList.get(1);
            } else {
                bvid = uri.getQueryParameter("video");
            }
            String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "token.txt";
            String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "csrf.txt";
            String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "cookie.txt";
            try {
                csrf = BilibiliPost.fileRead(csrfPath);
                toKen = BilibiliPost.fileRead(ToKenPath);
                cookie = BilibiliPost.fileRead(CookiePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new bvUrl().start();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.As_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        UserVideoAs();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.as_menu, menu);
        //Toolbar的搜索框
        MenuItem searchItem = menu.findItem(R.id.as_toolbar_search);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        SearchView finalSearchView = searchView;
        searchView.setIconified(false);
        //searchView 的 textView 控件绑定
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        searchView.setQueryHint("输入AV/BV/EP/SS或链接");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //折合SearchView
                if (query.length() > 0) {
                    //折合SearchView
                    finalSearchView.setIconified(true);
                }
                pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText EditText1 = (EditText) findViewById(R.id.As_EditText1);
                        bvid = query;
                        System.out.println(URL);
                        String pd = BilibiliPost.nav(toKen);
                        //判断登录是否正常
                        if (pd.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new bvUrl().start();
                                }
                            });
                        }
                    }
                }).start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //设置Menu点击事件
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.as_toolbar_goLike:
                if (aid != null) {
                    goLike();
                }
                break;
            case R.id.as_toolbar_GoAdd:
                if (aid != null) {
                    GoAdd();
                }
                break;
            case R.id.as_toolbar_GoTriple:
                if (aid != null) {
                    GoTriple();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;

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
        StatService.onPause(this);
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
        Jzvd.releaseAllVideos();
        //播放历史上报
        if (sharedPreferences.getBoolean("PlayReport", true)) {
            TextView currentTimeTextView = findViewById(R.id.current);
            BilibiliPost.AddVideoReport(aid, cid, currentTimeTextView.getText().toString(), csrf, cookie);
        }


    }


    private void TabLoad() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.As_TabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.As_ViewPager);
        ArrayList<String> tabList = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        tabList.add("推荐视频");
        tabList.add("视频评论");
        VideoComment mVideoComment = new VideoComment();
        VideoRecommend mVideoRecommend = new VideoRecommend();
        fragments.add(mVideoRecommend);
        fragments.add(mVideoComment);
        AsPagerAdapter myAssetPathFetcher = new AsPagerAdapter(getSupportFragmentManager(), fragments, tabList);
        mViewPager.setAdapter(myAssetPathFetcher);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    //这里是个人主页传回数据解析
    private void UserVideoAs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String UserVideoAid = intent.getStringExtra("UserVideoAid");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (UserVideoAid != null) {
                            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
                            bvid = UserVideoAid;
                            String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "token.txt";
                            String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "csrf.txt";
                            String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "cookie.txt";
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

    public void goUp(View view) {
        if (upMid != 0) {
            Intent intent = new Intent();
            intent.setClass(VideoAsActivity.this, UserActivity.class);
            intent.putExtra("cookie", cookie);
            intent.putExtra("mid", upMid + "");
            intent.putExtra("UP", "1");
            startActivity(intent);
        }

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
                    if (bvid.substring(0, 5).contains("https") || bvid.substring(0, 5).contains("http") || bvid.contains("bilibili.com") | bvid.contains("https://b23.tv/")) {
                        //输入一个视频链接，直接给截取公共解析
                        //这个方案存在问题，当输入错误链接异常时可能无法及时停止解析
                        if (bvid.contains("ep") || bvid.contains("EP")) {
                            epGo(bvid);
                        } else {
                            public_jx(bvid);
                        }
                    } else if (bvid.substring(0, 2).contains("av") || bvid.substring(0, 2).contains("AV")) {
                        //过滤掉多余的东西
                        if (bvid.contains("AV")) {
                            bvid = bvid.replaceAll("AV", "");
                        } else {
                            //如果不是，就直接让接口解析
                            bvid = bvid.replaceAll("av", "");
                        }
                        name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid, cookie);
                        abvGo(name);
                    } else if (bvid.substring(0, 2).contains("bv") || bvid.substring(0, 2).contains("BV")) {
                        name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid, cookie);
                        abvGo(name);
                    } else if (bvid.substring(0, 2).contains("ss") || bvid.substring(0, 2).contains("SS")) {
                        //输入一个ss的番剧链接，为了准确
                        if (!bvid.substring(0, 5).contains("https") && !bvid.contains("http")) {
                            bvid = "https://www.bilibili.com/bangumi/play/" + bvid;
                        }
                        public_jx(bvid);
                    } else if (bvid.substring(0, 2).contains("ep") || bvid.substring(0, 2).contains("EP")) {
                        bvid = "https://www.bilibili.com/bangumi/play/" + bvid;
                        epGo(bvid);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //关闭弹窗，提示失败
                                pd2.cancel();
                                Toast.makeText(getApplicationContext(), "看起来没有解析到-2", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    //全数字一定是av
                    name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid, cookie);
                    abvGo(name);
                }
            } catch (Exception e) {
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
                if (pd.equals("0")) {
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

    public void barrageGet(View view) {
        //下载弹幕
        pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在获取弹幕");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取下载路径
                    String DMUrl = "http://api.bilibili.com/x/v1/dm/list.so?oid=" + cid;
                    Connection.Response response = Jsoup.connect(DMUrl)
                            .method(Connection.Method.GET)
                            .timeout(5000)
                            .execute();
                    String DMXml = response.body().replace("</d>", "</d>\n");
                    String DownloadPath = sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath));
                    DownloadPath = DownloadPath + Title + type + cid + ".xml";
                    BilibiliPost.fileWrite(DownloadPath, DMXml);
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
    public void FMDownload(View view) {
        if (bvid != null) {
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
        } else {
            Toast.makeText(getApplicationContext(), "请先解析", Toast.LENGTH_SHORT).show();
        }
    }


    //图片保存方案
    //保存到本地
    public void savePhoto(Context context, Bitmap bitmap) {
        File photoDir = new File(Environment.getExternalStorageDirectory(), "DCIM/BILIBILIAS");
        if (!photoDir.exists()) {
            photoDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File photo = new File(photoDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(photo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updatePhotoMedia(photo, context);
    }


    //更新图库
    private static void updatePhotoMedia(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }


    public boolean isENChar(String string) {
        boolean flag = false;
        Pattern p = Pattern.compile("[a-zA-z]");
        if (p.matcher(string).find()) {
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
        Mid = sj(name, "mid\":", ",\"");
        up = sj(name, "\"name\":\"", "\",");
        Copyright = sj(name, "copyright\":", ",\"");
        System.out.println(aid);
        JSONObject json = new JSONObject(name);
        JSONObject data = json.getJSONObject("data");
        tName = data.getString("tname");
        JSONArray pages = data.getJSONArray("pages");
        JSONObject UPNameJson = data.getJSONObject("owner");
        final String UPUser = UPNameJson.getString("name");
        JSONObject videoStat = data.getJSONObject("stat");
        upMid = UPNameJson.getInt("mid");
        upFace = UPNameJson.getString("face");
        playVolume = videoStat.getInt("view");
        barrageVolume = videoStat.getInt("danmaku");
        upName = UPUser;
        String VideoDesc = data.getString("desc");
        System.out.println(pages);
        name = sj(name, "cid\":", ",\"");
        String VideoTitle = null;
        //初步获取视频分辨率
        jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json&fourk=1";
        String jsonStr = HttpUtils.doGet(jxUrl, cookie);
        System.out.println(jsonStr);
        //分辨率解析列表展示
        JSONObject jsonVideo = new JSONObject(jsonStr);
        JSONObject dataStr = jsonVideo.getJSONObject("data");
        JSONArray pagesVideo = dataStr.getJSONArray("accept_description");
        JSONArray quality = dataStr.getJSONArray("accept_quality");
        for (int i = 0; i < pagesVideo.length(); i++) {
            String hz = quality.getString(i);
            listVideo.add(pagesVideo.getString(i) + "[" + hz + "]");
        }
        //关闭弹窗，提示失败
        if (pages.length() == 1) {
            VideoTitle = pages.getString(0);
            JSONObject TitleJson = new JSONObject(VideoTitle);
            String TitleStr = TitleJson.getString("part");
            list.add("[" + name + "]" + TitleStr);
            listVideoName.add(TitleStr);
            System.out.println(ImageUrl);
        } else {
            for (int i = 0; i < pages.length(); i++) {
                JSONObject honor = pages.getJSONObject(i);
                String TitleStr = honor.getString("part");
                String cid1 = honor.getString("cid");
                list.add("[" + cid1 + "]" + TitleStr);
                listVideoName.add(TitleStr);
            }
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
        } else {
            videoView();
        }

    }

    //番剧单独解析方法
    private void epGo(String name) throws JSONException {
        //这里是判断一下番剧的数据
        //获取番剧页面源码
        String ep = HttpUtils.doGet(name, cookie);
        //截取需要的部分
        String epStr = sj(ep, "<script>window.__INITIAL_STATE__=", "</script>");
        Title = sj(epStr, "\"h1Title\":\"", "\",");
        //再单独切出aid，bvid，图片链接这些东西
        epStr = sj(epStr, "epInfo", "parentNode.removeChild");
        System.out.println(epStr);
        aid = sj(epStr, "aid\":", ",\"");
        bvid = sj(epStr, "bvid\":\"BV", "\",");
        Mid = sj(name, "mid\":", ",\"");
        Copyright = sj(name, "copyright\":", ",\"");
        ImageUrl = sj(epStr, "bangumi\",\"cover\":\"", "\",");
        //图片需要转码
        ImageUrl = "http:" + unicodeDecode(ImageUrl);
        //番剧列表化
        String epJson = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + aid, cookie);
        VideoJson = epJson;
        JSONObject json = new JSONObject(epJson);
        JSONObject data = json.getJSONObject("data");
        tName = data.getString("tname");
        JSONObject UPNameJson = data.getJSONObject("owner");
        JSONObject videoStat = data.getJSONObject("stat");
        upMid = UPNameJson.getInt("mid");
        upFace = UPNameJson.getString("face");
        playVolume = videoStat.getInt("view");
        barrageVolume = videoStat.getInt("danmaku");
        final String UPUser = UPNameJson.getString("name");
        upName = UPUser;
        String VideoDesc = data.getString("desc");
        JSONArray pages = data.getJSONArray("pages");
        String VideoTitle = null;
        System.out.println(pages);
        //截取标题 截取cid
        name = sj(epStr, "cid\":", ",\"");
        cid = name;
        //初步获取视频分辨率
        jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + name + "&bvid=" + bvid + "&type=json&fourk=1";
        String jsonStr = HttpUtils.doGet(jxUrl, cookie);
        System.out.println(jsonStr);
        //分辨率解析列表展示
        JSONObject jsonVideo = new JSONObject(jsonStr);
        JSONObject dataStr = jsonVideo.getJSONObject("data");
        JSONArray pagesVideo = dataStr.getJSONArray("accept_description");
        JSONArray quality = dataStr.getJSONArray("accept_quality");
        for (int i = 0; i < pagesVideo.length(); i++) {
            String hz = quality.getString(i);
            listVideo.add(pagesVideo.getString(i) + "[" + hz + "]");
        }
        //判断下这个截取的数据是不是空的
        //关闭弹窗，提示失败
        if (pages.length() == 1) {
            VideoTitle = pages.getString(0);
            JSONObject TitleJson = new JSONObject(VideoTitle);
            String TitleStr = TitleJson.getString("part");
            list.add("[" + name + "]" + TitleStr);
            listVideoName.add(TitleStr);
        } else {
            for (int i = 0; i < pages.length(); i++) {
                JSONObject honor = pages.getJSONObject(i);
                String TitleStr = honor.getString("part");
                String cid1 = honor.getString("cid");
                list.add("[" + cid1 + "]" + TitleStr);
                listVideoName.add(TitleStr);
            }
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
            //加载显示数据
            epView();
        }

    }

    private void videoView() {
        //控件定位
        LinearLayout batcLinearLayout = (LinearLayout) findViewById(R.id.As_batchDownload_LinearLayout);
        ImageView1 = (ImageView) findViewById(R.id.As_ImageView);
        TextView1 = (TextView) findViewById(R.id.As_Title);
        TextView2 = (TextView) findViewById(R.id.As_UP);
        TextView PlayText = (TextView) findViewById(R.id.As_Play);
        TextView DanMuText = (TextView) findViewById(R.id.As_DanMu);
        ImageView FaceImage = (ImageView) findViewById(R.id.As_Up_Face);
        final Bitmap bitmap = returnBitMap(ImageUrl);
        //显示番剧图片
        StrData = HttpUtils.doGet(jxUrl + "&fnval=1", cookie);
        System.out.println(jxUrl + "&fnval=1");
        StrData = sj(StrData, "url\":\"", "\",");
        StrData = unicodeDecode(StrData);
        //显示番剧图片
        ImageView1.post(new Runnable() {
            @Override
            public void run() {
                //加载推荐
                TabLoad();
                // TODO Auto-generated method stub
                TextView1.setText(Title);
                TextView2.setText(upName);
                //设置UP主信息
                Glide.with(VideoAsActivity.this).load(upFace).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(FaceImage);
                PlayText.setText(" 播放:" + VerificationUtils.DigitalConversion(playVolume) + " ");
                DanMuText.setText(" 弹幕:" + VerificationUtils.DigitalConversion(barrageVolume) + " ");
                ImageView1.setImageBitmap(bitmap);
                ListArray(list);
                ListArrayVideo(listVideo);
                listCode.add("mp4");
                listCode.add("mp4【音频视频分离下载最快】");
                listCode.add("flv");
                ListArrayCode(listCode);

                JZDataSource jzDataSource = new JZDataSource(StrData, Title);
                jzDataSource.headerMap.put("Cookie", cookie);
                jzDataSource.headerMap.put("Referer", "https://www.bilibili.com/video/av" + aid + "/");
                jzDataSource.headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
                jzVideoPlayerStandard = (JZPlay) findViewById(R.id.As_VideoPlayer);
                jzVideoPlayerStandard.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL);
                Glide.with(VideoAsActivity.this).load(ImageUrl).into(jzVideoPlayerStandard.posterImageView);
                //判断是否展示批量下载按钮
                if (list.size() > 1) {
                    batcLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    batchIF = false;
                    batcLinearLayout.setVisibility(View.GONE);
                }

            }
        });
        pd2.cancel();
    }

    //番剧解析显示数据
    private void epView() {
        //定位组件
        LinearLayout batcLinearLayout = (LinearLayout) findViewById(R.id.As_batchDownload_LinearLayout);
        ImageView1 = (ImageView) findViewById(R.id.As_ImageView);
        TextView1 = (TextView) findViewById(R.id.As_Title);
        TextView2 = (TextView) findViewById(R.id.As_UP);
        TextView PlayText = (TextView) findViewById(R.id.As_Play);
        TextView DanMuText = (TextView) findViewById(R.id.As_DanMu);
        ImageView FaceImage = (ImageView) findViewById(R.id.As_Up_Face);
        final Bitmap bitmap = returnBitMap(ImageUrl);
        //显示番剧图片
        StrData = HttpUtils.doGet(jxUrl + "&fnval=1", cookie);
        System.out.println(jxUrl);
        StrData = sj(StrData, "url\":\"", "\",");
        StrData = unicodeDecode(StrData);
        //显示番剧图片
        ImageView1.post(new Runnable() {
            @Override
            public void run() {
                //加载推荐
                TabLoad();
                // TODO Auto-generated method stub
                TextView1.setText(Title);
                TextView2.setText(upName);
                //设置UP主信息
                Glide.with(VideoAsActivity.this).load(upFace).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(FaceImage);
                PlayText.setText(" 播放:" + playVolume + " ");
                DanMuText.setText(" 弹幕:" + barrageVolume + " ");
                ImageView1.setImageBitmap(bitmap);
                ListArray(list);
                ListArrayVideo(listVideo);
                listCode.add("mp4");
                listCode.add("mp4【音频视频分离下载最快】");
                listCode.add("flv");
                ListArrayCode(listCode);

                //JZ
                JZDataSource jzDataSource = new JZDataSource(StrData, Title);
                jzDataSource.headerMap.put("Cookie", cookie);
                jzDataSource.headerMap.put("Referer", "https://www.bilibili.com/video/av" + aid + "/");
                jzDataSource.headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
                jzVideoPlayerStandard = (JZPlay) findViewById(R.id.As_VideoPlayer);
                jzVideoPlayerStandard.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL);
                Glide.with(VideoAsActivity.this).load(ImageUrl).into(jzVideoPlayerStandard.posterImageView);
                //判断是否展示批量下载按钮
                if (list.size() > 1) {
                    batcLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    batchIF = false;
                    batcLinearLayout.setVisibility(View.GONE);
                }

            }
        });
        pd2.cancel();
    }

    private void public_jx(String name) throws JSONException {
        name = HttpUtils.doGet(name, cookie);
        //String newEpUrl = sj(name, "<script type=\"application/ld+json\">", "</script>");
        name = sj(name, "<script>window.__INITIAL_STATE__=", ";(function");

        if (name.contains("couponSelected")) {
            String finalName = name;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject epJson = null;
                    try {
                        epJson = new JSONObject(finalName);
                        JSONArray epArray = epJson.getJSONArray("epList");
                        List<String> epAsAidArray = new ArrayList<String>();
                        List<String> epAsSetArray = new ArrayList<String>();
                        epAsSetArray.clear();
                        epAsAidArray.clear();
                        final String[] names = new String[epArray.length()];
                        for (int i = 0; i < epArray.length(); i++) {
                            JSONObject VideoData = epArray.getJSONObject(i);
                            int avid = VideoData.getInt("aid");
                            String titleFormat = VideoData.getString("titleFormat");
                            String longTitle = VideoData.getString("longTitle");
                            String badge = VideoData.getString("badge");
                            names[i] = titleFormat + " " + longTitle + " " + badge;
                            epAsAidArray.add(avid + "");
                        }
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VideoAsActivity.this);
                        builder.setTitle("请选择解析的子集");
                        //设置Dialog为多选框，且无默认选项（null）
                        builder.setMultiChoiceItems(names, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            //设置点击事件：如果选中则添加进choose，如果取消或者未选择则移出choose
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    epAsSetArray.add(epAsAidArray.get(which));
                                } else {
                                    epAsSetArray.remove(epAsAidArray.get(which));
                                }
                            }
                        });
                        //设置正面按钮以及点击事件（土司显示choose内容）
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(VideoAsActivity.this, "选取完成", Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + epAsSetArray.get(0), cookie);
                                        try {
                                            abvGo(name);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        });
                        builder.show();//显示Dialog对话框
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            bvid = sj(name, "aid\":", ",");
            name = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + bvid, cookie);
            abvGo(name);
        }


    }


    //下面是一些对视频点赞/投币等操作

    //点赞
    private void goLike() {
        danmakuView.removeAllDanmakus(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pd = BilibiliPost.Like(bvid, toKen, csrf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pd.equals("0")) {
                            Toast.makeText(getApplicationContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else if (pd.equals("65006")) {
                            Toast.makeText(getApplicationContext(), "我的妈天瓜子，不能重复点赞的", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "点赞失败了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    //投币方法
    private void GoAdd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pd = BilibiliPost.add(bvid, "1", toKen, csrf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pd.equals("0")) {
                            Toast.makeText(getApplicationContext(), "投币成功", Toast.LENGTH_SHORT).show();
                        } else if (pd.equals("-104")) {
                            Toast.makeText(getApplicationContext(), "出大问题，大佬你似乎没硬币了，这下惨了", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "投币失败了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    //三连方法
    private void GoTriple() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pd = BilibiliPost.triple(bvid, toKen, csrf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pd.equals("0")) {
                            Toast.makeText(getApplicationContext(), "三连成功感谢推荐", Toast.LENGTH_SHORT).show();
                        } else if (pd.equals("-101")) {
                            Toast.makeText(getApplicationContext(), "出大问题，登录出问题了", Toast.LENGTH_SHORT).show();
                        } else {
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
            //判断dash的下载内容
            if (dashCount == 1) {
                StrData = dashUrlList[1];
                dashCount = 2;
            } else {
                StrData = HttpUtils.doGet(jxUrl, cookie);
                System.out.println(jxUrl);
                if (dashState) {
                    JSONObject VideoJson = null;
                    try {
                        VideoJson = new JSONObject(StrData);
                        VideoJson = VideoJson.getJSONObject("data");
                        JSONArray supportFormats = VideoJson.getJSONArray("support_formats");
                        VideoJson = VideoJson.getJSONObject("dash");
                        JSONArray AudioArray = VideoJson.getJSONArray("audio");
                        JSONArray VideoArray = VideoJson.getJSONArray("video");
                        JSONObject VideoUrl = VideoArray.getJSONObject(0);
                        for (int i = 0; i < VideoArray.length(); i++) {
                            VideoUrl = VideoArray.getJSONObject(i);
                            VideoId = VideoUrl.getInt("id");
                            if (qn.equals(VideoId + "")) {
                                VideoUrl = VideoArray.getJSONObject(i);
                                i = VideoArray.length();
                            }
                        }
                        for (int i = 0; i < supportFormats.length(); i++) {

                            JSONObject displayDescJson = supportFormats.getJSONObject(i);
                            if (qn.equals(displayDescJson.getInt("quality") + "")) {
                                displayDesc = displayDescJson.getString("display_desc");
                                i = supportFormats.length();
                            }
                        }

                        JSONObject AudioUrl = AudioArray.getJSONObject(0);
                        dashUrlList[0] = AudioUrl.getString("baseUrl");
                        dashUrlList[1] = VideoUrl.getString("baseUrl");
                        StrData = dashUrlList[0];
                        dashCount = 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //StrData = HttpUtils.doGet(jxUrl, cookie);
                    try {
                        //视频参数1
                        JSONObject VideoJson = new JSONObject(StrData);
                        VideoJson = VideoJson.getJSONObject("data");
                        JSONArray VideoArray = VideoJson.getJSONArray("durl");
                        if (VideoArray.length() >= 0) {
                            VideoJson = VideoArray.getJSONObject(0);
                            StrData = VideoJson.getString("url");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            RequestParams params = new RequestParams(StrData);
            //检测突破
            params.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            params.addHeader("referer", "https://www.bilibili.com/video/av" + aid + "/");
            params.addHeader("Cookie", cookie);
            //获取断点配置
            //DownloadPath
            boolean AutoResume = sharedPreferences.getBoolean("dl_switch", true);
            String DownloadPath = sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath));
            String DownloadName = fileRename(sharedPreferences.getString("DownloadName", "P{P}-{P_TITLE}-{CID}.{VIDEO_TYPE}"));
            params.setAutoResume(AutoResume);//设置是否在下载是自动断点续传
            params.setAutoRename(false);//设置是否根据头信息自动命名文件
            DLPath = DownloadPath;
            Log.e("命名规则", DownloadName);

            if (dashState) {
                if (dashCount == 1) {
                    DLPath = DLPath + DownloadName + "_.aac";
                    dashAudioPath = DLPath;
                } else {
                    DLPath = DLPath + DownloadName + "_.mp4";
                    dashVideoPath = DLPath;
                }
            } else {
                DLPath = DLPath + DownloadName;
                dashVideoPath = DLPath;
            }
            params.setSaveFilePath(DLPath);//设置下载地址
            params.setExecutor(new PriorityExecutor(3, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
            params.setCancelFast(true);//是否可以被立即停止.
            //下面的回调都是在主线程中运行的,这里设置的带进度的回调
            cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onCancelled(CancelledException arg0) {
                    Log.i("tag", "取消" + Thread.currentThread().getName());
                    Toast.makeText(getApplicationContext(), "下载已取消", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    Log.i("tag", "onError: 失败" + Thread.currentThread().getName());
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    mMessageDialog.dismiss();
                }

                @Override
                public void onFinished() {
                    Log.i("tag", "完成,每次取消下载也会执行该方法" + Thread.currentThread().getName());
                    mMessageDialog.dismiss();
                }

                @Override
                public void onSuccess(File arg0) {
                    Log.i("tag", "下载成功的时候执行" + Thread.currentThread().getName());
                    Toast.makeText(getApplicationContext(), Title + batchFor + "下载完成", Toast.LENGTH_SHORT).show();
                    setProgressBar("下载完成", 100, batchFor);

                    progressDialog.dismiss();
                    //判断是否有批量下载
                    if (batchIF) {
                        if (batchFor < listBatch.size()) {
                            progressDialog.dismiss();
                            String data = listBatch.get(batchFor);
                            cid = sj(data, "[", "]");
                            batchFor = batchFor + 1;
                            jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
                            System.out.println(jxUrl);
                            initProgressDialog();
                            newProgressBar(Title, batchFor);
                            new download().start();
                        }
                        //判断是否有分离下载
                    } else if (dashCount == 2) {
                        dashState = false;
                        dashCount = 0;
                        runOnUiThread(() -> {
                            MessageDialog.build()
                                    .setTitle("导入提示")
                                    .setMessage("嘿！这是一个新的实验室功能，我们将把你下载的音频和视频导入哔哩哔哩缓存列表，以此带来更好的播放观看体验，请选择")
                                    //设置按钮文本并设置回调
                                    .setOkButton("导入B站", new OnDialogButtonClickListener<MessageDialog>() {
                                        @Override
                                        public boolean onClick(MessageDialog baseDialog, View v) {
                                            videoImport();
                                            return false;
                                        }
                                    })
                                    .setCancelButton("不导入", null).show();
                        });


                    } else if (dashState) {
                        initProgressDialog();
                        new download().start();
                        //提醒用户合并视频
                    }
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    if (isDownloading) {
                        double videoFileMb = (double) (total / 1048576);
                        double videoFileMbDl = (double) (current / 1048576);
                        mMessageDialog.setMessage("视频大小" + videoFileMb + "MB\n" + "当前下载" + videoFileMbDl + "MB\n" + "当前下载地址\n" + DLPath + "\n进入设置页面设置下载地址");
                        progressBar.setProgress((int) (current * 100 / total));
                        setProgressBar(Title + batchFor, (int) (current * 100 / total), batchFor);
                        Log.i("tag", "下载中,会不断的进行回调:" + Thread.currentThread().getName());
                    }
                }

                @Override
                public void onStarted() {
                    Log.i("tag", "开始下载的时候执行" + Thread.currentThread().getName());
                    mMessageDialog = MessageDialog.show("下载提示", "当前下载地址\n" + DLPath + "\n进入设置页面设置下载地址", "暂停", "隐藏下载")
                            //设置自定义布局
                            //空白不可取消
                            .setCancelable(false)
                            .setCustomView(new OnBindView<MessageDialog>(progressBar) {
                                @Override
                                public void onBind(MessageDialog dialog, View v) {
                                    //添加布局边距
                                    dialog.getDialogImpl().boxCustom.setPadding(dip2px(20), dip2px(10), dip2px(20), dip2px(10));
                                }
                            })
                            .setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                                @Override
                                public boolean onClick(MessageDialog baseDialog, View v) {
                                    //点击取消正在下载的操作
                                    //结束循环
                                    batchIF = false;
                                    batchFor = 1;
                                    cancelable.cancel();
                                    return false;
                                }
                            });
                }

                @Override
                public void onWaiting() {
                    Log.i("tag", "等待,在onStarted方法之前执行" + Thread.currentThread().getName());
                }

            });
        }
    }

    //初始化进度
    private void newProgressBar(String Name, int ID) {
        Intent intent = new Intent(this, VideoAsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        channelId = createNotificationChannel("BILIBILIAS_ProgressBar_ID" + ID, "BILIBILIAS_ProgressBar_Name" + ID, NotificationManager.IMPORTANCE_HIGH);
        notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("下载通知")
                .setContentText(Name)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        notification.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(ID, notification.build());
    }

    private void setProgressBar(String Name, int progress, int ID) {
        //更新
        notification.setContentText(Name).setProgress(100, progress, false);
        notificationManager.notify(ID, notification.build());
    }


    private String createNotificationChannel(String channelID, String channelNAME, int level) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }


    private String fileRename(String DownloadName) {
        DownloadName = DownloadName.replace("{AV}", aid);
        DownloadName = DownloadName.replace("{BV}", bvid);
        DownloadName = DownloadName.replace("{P_TITLE}", listVideoName.get(batchFor - 1));
        DownloadName = DownloadName.replace("{CID}", cid);
        DownloadName = DownloadName.replace("{VIDEO_TYPE}", videoType);
        DownloadName = DownloadName.replace("{P}", batchFor + "");
        DownloadName = DownloadName.replace("{TITLE}", Title);
        DownloadName = DownloadName.replace("{TYPE}", type);
        DownloadName = DownloadName.replaceAll("/", " ");
        return DownloadName;
    }

    //视频缓存导入
    private void videoImport() {
        pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在获取必要数据");
        new Thread(() -> {
            String FMJsonStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/view?bvid=" + bvid, cookie);
            String FMUrl = null;
            String VideoData = HttpUtils.doGet("https://api.bilibili.com/x/player/pagelist?bvid=" + bvid + "&jsonp=jsonp", cookie);
            try {
                JSONObject newVideoJson = new JSONObject(VideoData);
                JSONArray newVideoArray = newVideoJson.getJSONArray("data");
                newVideoJson = newVideoArray.getJSONObject(0);
                JSONObject dimensionJson = newVideoJson.getJSONObject("dimension");
                int height = dimensionJson.getInt("height");
                int width = dimensionJson.getInt("width");
                int cid = newVideoJson.getInt("cid");
                String VideoEntry = getString(R.string.VideoEntry);
                String VideoIndex = getString(R.string.VideoIndex);
                VideoEntry = VideoEntry.replace("AID编号", aid);
                VideoEntry = VideoEntry.replace("BVID编号", bvid);
                VideoEntry = VideoEntry.replace("CID编号", cid + "");
                VideoEntry = VideoEntry.replace("下载标题", Title + ".mp4");
                VideoEntry = VideoEntry.replace("文件名称", Title + ".mp4");
                VideoEntry = VideoEntry.replace("标题", Title);
                VideoEntry = VideoEntry.replace("高度", height + "");
                VideoEntry = VideoEntry.replace("宽度", width + "");
                VideoEntry = VideoEntry.replace("QN编码", qn + "");
                VideoEntry = VideoEntry.replace("下载子标题", listVideoName.get(batchFor - 1) + ".mp4");

                //封面获取
                JSONObject FMJson = new JSONObject(FMJsonStr);
                FMJson = FMJson.getJSONObject("data");
                FMUrl = FMJson.getString("pic");
                FMUrl = FMUrl.replace("/", "\\/");


                String ImportPath = sharedPreferences.getString("setting_set_Import", "/storage/emulated/0/Android/data/");


                //储存本地信息
                int dashAudioSize = AppFilePathUtils.getFileSize(dashAudioPath);
                int dashVideoSize = AppFilePathUtils.getFileSize(dashVideoPath);
                VideoEntry = VideoEntry.replace("封面地址", FMUrl);
                VideoEntry = VideoEntry.replace("下载大小", dashVideoSize + "");
                VideoIndex = VideoIndex.replace("视频大小", dashVideoSize + "");
                VideoEntry = VideoEntry.replace("清晰度", displayDesc + "");
                VideoIndex = VideoIndex.replace("QN编码", qn + "");
                VideoIndex = VideoIndex.replace("音频大小", dashAudioSize + "");

                BilibiliPost.fileWrite(getExternalFilesDir("哔哩哔哩视频").toString() + "/导入模板/entry.json", VideoEntry);
                BilibiliPost.fileWrite(getExternalFilesDir("哔哩哔哩视频").toString() + "/导入模板/index.json", VideoIndex);

                //文件移动
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //安卓Q开始切换储存模式
                    pd2.cancel();
                    MessageDialog.build()
                            .setTitle("权限申请")
                            .setMessage("请务必点击授权访问，这个是为了导入文件，安卓10开始必须要授权data目录权限。\n\n如果你没有阅读新版本导入介绍，请务必先阅读，否则导入将失败。")
                            //设置按钮文本并设置回调
                            .setOkButton("授权访问", new OnDialogButtonClickListener<MessageDialog>() {
                                @Override
                                public boolean onClick(MessageDialog baseDialog, View v) {
                                    fileUriUtils.startFor("/storage/emulated/0/Android/data/", VideoAsActivity.this, 3);
                                    return false;
                                }
                            })
                            .setOtherButton("导入教程", new OnDialogButtonClickListener<MessageDialog>() {
                                @Override
                                public boolean onClick(MessageDialog baseDialog, View v) {
                                    Uri uri = Uri.parse("https://support.qq.com/products/337496/faqs/99945");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    return true;
                                }
                            })
                            .setCancelButton("取消", null).show();
                } else {
                    pd2.cancel();
                    String finalVideoEntry = VideoEntry;
                    String finalVideoIndex = VideoIndex;
                    MessageDialog.build()
                            .setTitle("路径锁定")
                            .setMessage("请点击确认，并且输入你需要替换的缓存目录的AV编号。\n\n如果你不明白在说什么，请点击导入教程，否则不按规定会导入失败")
                            //设置按钮文本并设置回调
                            .setOkButton("确定", new OnDialogButtonClickListener<MessageDialog>() {
                                @Override
                                public boolean onClick(MessageDialog baseDialog, View v) {
                                    new InputDialog("被替换缓存视频AV编号", "请输入要替换的视频缓存的对应缓存文件夹AV编号", "确定", "取消", null)
                                            .setCancelable(false)
                                            .setInputHintText("10086")
                                            .setOkButton((baseDialog1, v1, inputStr) -> {
                                                pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在复制文件");
                                                new Thread(() -> {
                                                    String videoStr = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/view?aid=" + inputStr, cookie);
                                                    try {
                                                        JSONObject videoJson = new JSONObject(videoStr);
                                                        videoJson = videoJson.getJSONObject("data");
                                                        int mCid = videoJson.getInt("cid");
                                                        runOnUiThread(() -> {
                                                            //Android10以下走普通渠道
                                                            try {
                                                                BilibiliPost.fileWrite("/storage/emulated/0/Android/data/tv.danmaku.bili/download/" + inputStr + "/" + "c_" + mCid + "/entry.json", finalVideoEntry);
                                                                BilibiliPost.fileWrite("/storage/emulated/0/Android/data/tv.danmaku.bili/download/" + inputStr + "/" + "c_" + mCid + "/" + qn + "/index.json", finalVideoIndex);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            AppFilePathUtils.copyFile(getExternalFilesDir("哔哩哔哩视频").toString() + "/DM.xml", "/storage/emulated/0/Android/data/tv.danmaku.bili/download/" + inputStr + "/" + "c_" + cid + "/danmaku.xml");
                                                            AppFilePathUtils.copyFile(dashVideoPath, "/storage/emulated/0/Android/data/tv.danmaku.bili/download/" + inputStr + "/" + "c_" + mCid + "/" + qn + "/video.m4s");
                                                            AppFilePathUtils.copyFile(dashAudioPath, "/storage/emulated/0/Android/data/tv.danmaku.bili/download/" + inputStr + "/" + "c_" + mCid + "/" + qn + "/audio.m4s");
                                                            boolean ImportDelete = sharedPreferences.getBoolean("ImportDelete", false);
                                                            if (ImportDelete) {
                                                                BilibiliPost.deleteFile(dashVideoPath);
                                                                BilibiliPost.deleteFile(dashAudioPath);
                                                            }
                                                            Toast.makeText(getApplicationContext(), "复制完成", Toast.LENGTH_SHORT).show();
                                                            pd2.cancel();
                                                        });
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }).start();
                                                return false;
                                            })
                                            .show();
                                    return false;

                                }
                            })
                            .setOtherButton("导入教程", (baseDialog, v) -> {
                                Uri uri = Uri.parse("https://support.qq.com/products/337496/faqs/99945");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                return true;
                            })
                            .setCancelButton("取消", null).show();


                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                pd2.cancel();
                Toast.makeText(getApplicationContext(), "获取完成", Toast.LENGTH_SHORT).show();
            });
        }).start();

    }


    //返回授权状态
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        //使用resultdata.getdata ( )提取该URI
        super.onActivityResult(requestCode, resultCode, resultData);
        int AndroidR_File_ID = 1;
        int SetDlPath_ID = 2;
        int SetAvid_ID = 3;
        int Set_Video = 4;
        int Set_Audio = 5;
        int Set_Entry = 6;
        int Set_Index = 7;
        int Set_Danmaku = 8;

        if (requestCode == AndroidR_File_ID && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {

                uri = resultData.getData();
                //关键是这里，这个就是保存这个目录的访问权限
                getContentResolver().takePersistableUriPermission(uri, resultData.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

                //安卓Q开始切换储存模式
                MessageDialog.build()
                        .setTitle("选择替换缓存目录")
                        .setMessage("【认真看】：请选择一个B站缓存视频文件夹。\n\n什么是缓存视频文件夹？\n导入缓存的前提是必须存在一个B站的视频缓存文件，因此，你需要先缓存一个视频。\n缓存后，在B站缓存文件夹下面会有一个以缓存视频AV号为名称的文件夹，请选择它。")
                        //设置按钮文本并设置回调
                        .setOkButton("选择缓存替换目录", new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog baseDialog, View v) {
                                fileUriUtils.startFor("/storage/emulated/0/Android/data/tv.danmaku.bili/download/", VideoAsActivity.this, 3);
                                return false;
                            }
                        })
                        .setCancelButton("取消", null).show();

                //普通目录权限获取
                //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //过滤器只显示可以打开的结果
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                //使用图像MIME数据类型过滤以仅显示图像
                //intent.setType("*/*");
                //要搜索通过已安装的存储提供商提供的所有文档
                //intent.setType("*/*");
                //startActivityForResult(intent, 2);

                Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent1, 2);

                Log.i("URI", "Uri: " + uri.toString());


            }
        } else if (requestCode == Set_Danmaku && resultCode == Activity.RESULT_OK) {
            Uri uri = resultData.getData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppFilePathUtils.copySafFile(getExternalFilesDir("哔哩哔哩视频").toString() + "/DM.xml", uri, VideoAsActivity.this);
                AppFilePathUtils.copySafFile(getExternalFilesDir("哔哩哔哩视频").toString() + "/导入模板/entry.json", SAF_Entry, VideoAsActivity.this);
                AppFilePathUtils.copySafFile(getExternalFilesDir("哔哩哔哩视频").toString() + "/导入模板/index.json", SAF_Index, VideoAsActivity.this);

                AppFilePathUtils.copySafFile(dashVideoPath, SAF_Video, VideoAsActivity.this);
                AppFilePathUtils.copySafFile(dashAudioPath, SAF_Audio, VideoAsActivity.this);

                Toast.makeText(getApplicationContext(), "导入完成", Toast.LENGTH_SHORT).show();
                boolean ImportDelete = sharedPreferences.getBoolean("ImportDelete", false);
                if (ImportDelete) {
                    BilibiliPost.deleteFile(dashVideoPath);
                    BilibiliPost.deleteFile(dashAudioPath);
                }
            }


        } else if (requestCode == Set_Index && resultCode == Activity.RESULT_OK) {

            SAF_Entry = resultData.getData();

            //安卓Q开始切换储存模式
            MessageDialog.build()
                    .setTitle("选择替换弹幕文件")
                    .setMessage("在下面授权中，请选择danmaku.xml文件，来确保完成替换")
                    //设置按钮文本并设置回调
                    .setOkButton("选中替换文件", new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            //普通目录权限获取
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            //过滤器只显示可以打开的结果
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            //使用图像MIME数据类型过滤以仅显示图像
                            intent.setType("*/*");
                            //要搜索通过已安装的存储提供商提供的所有文档
                            intent.setType("*/*");
                            startActivityForResult(intent, 8);
                            return false;
                        }
                    })
                    .setCancelButton("取消", null).show();

        } else if (requestCode == Set_Entry && resultCode == Activity.RESULT_OK) {

            SAF_Index = resultData.getData();

            //安卓Q开始切换储存模式
            MessageDialog.build()
                    .setTitle("选择替换配置文件2")
                    .setMessage("在下面授权中，请选择entry.json文件，来确保完成替换")
                    //设置按钮文本并设置回调
                    .setOkButton("选中替换文件", new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            //普通目录权限获取
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            //过滤器只显示可以打开的结果
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            //使用图像MIME数据类型过滤以仅显示图像
                            intent.setType("*/*");
                            //要搜索通过已安装的存储提供商提供的所有文档
                            intent.setType("*/*");
                            startActivityForResult(intent, 7);
                            return false;
                        }
                    })
                    .setCancelButton("取消", null).show();

        } else if (requestCode == Set_Audio && resultCode == Activity.RESULT_OK) {

            SAF_Audio = resultData.getData();


            //安卓Q开始切换储存模式
            MessageDialog.build()
                    .setTitle("选择替换配置文件1")
                    .setMessage("在下面授权中，请选择index.json文件，来确保完成替换")
                    //设置按钮文本并设置回调
                    .setOkButton("选中替换文件", new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            //普通目录权限获取
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            //过滤器只显示可以打开的结果
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            //使用图像MIME数据类型过滤以仅显示图像
                            intent.setType("*/*");
                            //要搜索通过已安装的存储提供商提供的所有文档
                            intent.setType("*/*");
                            startActivityForResult(intent, 6);
                            return false;
                        }
                    })
                    .setCancelButton("取消", null).show();

        } else if (requestCode == Set_Video && resultCode == Activity.RESULT_OK) {

            SAF_Video = resultData.getData();

            //安卓Q开始切换储存模式
            MessageDialog.build()
                    .setTitle("选择替换音频文件")
                    .setMessage("在下面授权中，请选择audio.m4s文件，来确保完成替换")
                    //设置按钮文本并设置回调
                    .setOkButton("选中替换文件", new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            //普通目录权限获取
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            //过滤器只显示可以打开的结果
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            //使用图像MIME数据类型过滤以仅显示图像
                            intent.setType("*/*");
                            //要搜索通过已安装的存储提供商提供的所有文档
                            intent.setType("*/*");
                            startActivityForResult(intent, 5);
                            return false;
                        }
                    })
                    .setCancelButton("取消", null).show();


        } else if (requestCode == SetAvid_ID && resultCode == Activity.RESULT_OK) {
            //获取视频CID
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Uri finalUri = uri;

                //安卓Q开始切换储存模式
                MessageDialog.build()
                        .setTitle("选择替换视频文件")
                        .setMessage("在下面授权中，请选择video.m4s文件，来确保完成替换")
                        //设置按钮文本并设置回调
                        .setOkButton("选中替换文件", new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog baseDialog, View v) {
                                //普通目录权限获取
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                //过滤器只显示可以打开的结果
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                //使用图像MIME数据类型过滤以仅显示图像
                                intent.setType("*/*");
                                //要搜索通过已安装的存储提供商提供的所有文档
                                intent.setType("*/*");
                                startActivityForResult(intent, 4);
                                return false;
                            }
                        })
                        .setCancelButton("取消", null).show();

                //setDLPath(uri);
                Log.i("URI", "Uri: " + uri.toString());
            }
        }


    }


    /**
     * 下面分别对应着不同格式的按钮下载
     */
    public void onBvUrl1(View view) {
        if (bvid != null) {
            initProgressDialog();
            pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "正在拉取数据");
            //下载视频
            downloadVideo();
        } else {
            Toast.makeText(getApplicationContext(), "你需要输入点东西", Toast.LENGTH_SHORT).show();
        }
    }

    //视频检查第哦啊有
    private void downloadVideo() {
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
                            if (pdVideo.equals("1")) {
                                AlertDialog aldg;
                                AlertDialog.Builder adBd = new AlertDialog.Builder(VideoAsActivity.this);
                                adBd.setTitle("警告");
                                adBd.setMessage("本视频UP主声明了，“未经作者授权，禁止转载”，请注意哦。\n违反规定造成一切后果自行承担。\n是否继续下载？？");
                                adBd.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SoFreeze();
                                    }
                                });
                                adBd.setNegativeButton("取消", null);
                                aldg = adBd.create();
                                aldg.show();
                            } else {
                                SoFreeze();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //题目验证
    private void verification() {
        try {
            JSONObject csJson = VerificationUtils.derivatives();
            String nr = csJson.getString("problem");
            String da = csJson.getString("answer");
            android.app.AlertDialog dialog1 = new android.app.AlertDialog.Builder(VideoAsActivity.this)
                    .setTitle("验证问题")
                    .setMessage(nr + "\n\n" + da)
                    .setPositiveButton("填写答案", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText inputAddFreezeVideo = new EditText(VideoAsActivity.this);
                            inputAddFreezeVideo.setHint("请输入结果数字");
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoAsActivity.this);
                            builder.setView(inputAddFreezeVideo);
                            builder.setTitle("填写答案")
                                    .setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                                    if (inputAddFreezeVideo.getText().toString().equals(da)) {
                                        SoFreeze();
                                        //步骤2： 实例化SharedPreferences.Editor对象
                                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //步骤3：将获取过来的值放入文件
                                        //editor.putString("verification", "1");
                                        //步骤4：提交 commit有返回值apply没有
                                        //editor.apply();
                                    } else {
                                        Toast.makeText(VideoAsActivity.this, "答案错误", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            builder.show();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .setNeutralButton(null, null)
                    .create();
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //解析判断
    private void SoFreeze() {
        pd2 = ProgressDialog.show(VideoAsActivity.this, "提示", "验证数据合法性");
        new Thread(() -> {
            System.out.println("Bvid=" + bvid + "&Mid=" + Mid);
            String SoFreezeStr = HttpUtils.doPost("https://api.misakaloli.com/app/AppFunction.php?type=SoFreeze", "Bvid=" + bvid + "&Mid=" + Mid, "");
            Log.e("冻结测试", SoFreezeStr);
            try {
                JSONObject SoFreezeJson = new JSONObject(SoFreezeStr);
                int code = SoFreezeJson.getInt("code");
                String msg = SoFreezeJson.getString("msg");
                runOnUiThread(() -> {
                    if (code == 0) {
                        pd2.cancel();
                        AddVideo();
                        if (DownloadMethod.equals("1")) {
                            newProgressBar(Title, batchFor);
                            new download().start();
                        } else if (DownloadMethod.equals("2")) {
                            videoDownloadNotification();
                        } else if (DownloadMethod.equals("3")) {
                            callAdmDownload();
                        } else if (DownloadMethod.equals("4")) {
                            callIdmDownload();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        pd2.cancel();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void callAdmDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StrData = HttpUtils.doGet(jxUrl, cookie);
                try {
                    JSONObject videoDataJson = new JSONObject(StrData);
                    videoDataJson = videoDataJson.getJSONObject("data");
                    JSONArray videoDataArray = videoDataJson.getJSONArray("durl");
                    videoDataJson = videoDataArray.getJSONObject(0);
                    String videoUrl = videoDataJson.getString("url");
                    System.out.println(videoUrl);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.APP_BROWSER");
                            intent.setData(Uri.parse(videoUrl));
                            intent.putExtra("Cookie", cookie);
                            intent.putExtra("Referer", "https://www.bilibili.com/video/av" + aid + "/");
                            intent.putExtra("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
                            if (AppFilePathUtils.isInstallApp(VideoAsActivity.this, "com.dv.adm")) {
                                Toast.makeText(VideoAsActivity.this, "正在拉起ADM", Toast.LENGTH_SHORT).show();
                                // 下载调用
                                String str1 = "com.dv.adm";
                                String str2 = "com.dv.adm.AEditor";
                                intent.setClassName(str1, str2);
                                startActivity(intent);
                            } else if (AppFilePathUtils.isInstallApp(VideoAsActivity.this, "com.dv.adm.pay")) {
                                Toast.makeText(VideoAsActivity.this, "正在拉起ADM PRO", Toast.LENGTH_SHORT).show();
                                // 下载调用
                                String str1 = "com.dv.adm.pay";
                                String str2 = "com.dv.adm.pay.AEditor";
                                intent.setClassName(str1, str2);
                                startActivity(intent);
                            } else {
                                Toast.makeText(VideoAsActivity.this, "看起来你还没有安装下载器", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void callIdmDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StrData = HttpUtils.doGet(jxUrl, cookie);
                try {
                    JSONObject videoDataJson = new JSONObject(StrData);
                    videoDataJson = videoDataJson.getJSONObject("data");
                    JSONArray videoDataArray = videoDataJson.getJSONArray("durl");
                    videoDataJson = videoDataArray.getJSONObject(0);
                    String videoUrl = videoDataJson.getString("url");
                    System.out.println(videoUrl);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.APP_BROWSER");
                            intent.setData(Uri.parse(videoUrl));
                            intent.putExtra("Cookie", cookie);
                            intent.putExtra("Referer", "https://www.bilibili.com/video/av" + aid + "/");
                            intent.putExtra("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
                            if (AppFilePathUtils.isInstallApp(VideoAsActivity.this, "idm.internet.download.manager.plus")) {
                                Toast.makeText(VideoAsActivity.this, "正在拉起IDM", Toast.LENGTH_SHORT).show();
                                // 下载调用
                                String str2 = "idm.internet.download.manager.plus";
                                intent.setClassName(str2, "idm.internet.download.manager.Downloader");
                                startActivity(intent);

                            } else if (AppFilePathUtils.isInstallApp(VideoAsActivity.this, "idm.internet.download.manager")) {
                                Toast.makeText(VideoAsActivity.this, "正在拉起IDM", Toast.LENGTH_SHORT).show();
                                // 下载调用
                                String str2 = "idm.internet.download.manager";
                                intent.setClassName(str2, "idm.internet.download.manager.Downloader");
                                startActivity(intent);
                            } else {
                                Toast.makeText(VideoAsActivity.this, "看起来你还没有安装下载器", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void videoDownloadNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StrData = HttpUtils.doGet(jxUrl, cookie);
                try {
                    JSONObject videoDataJson = new JSONObject(StrData);
                    videoDataJson = videoDataJson.getJSONObject("data");
                    JSONArray videoDataArray = videoDataJson.getJSONArray("durl");
                    videoDataJson = videoDataArray.getJSONObject(0);
                    String videoUrl = videoDataJson.getString("url");
                    System.out.println(videoUrl);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DownloadManagerUtil downloadManagerUtil = new DownloadManagerUtil(VideoAsActivity.this);
                            String DownloadName = fileRename(sharedPreferences.getString("DownloadName", "P{P}-{P_TITLE}-{CID}.{VIDEO_TYPE}"));
                            downloadManagerUtil.download(videoUrl, Title, "BILIBILI AS", DownloadName, cookie, aid);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //批量下载按钮
    public void batchDownloadVideo(View view) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("请选择下载子集");
        final String[] names = list.toArray(new String[list.size()]);
        listBatch.clear();
        listVideoName.clear();
        //设置Dialog为多选框，且无默认选项（null）
        builder.setMultiChoiceItems(names, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            //设置点击事件：如果选中则添加进choose，如果取消或者未选择则移出choose
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    listBatch.add(names[which]);
                    listVideoName.add(names[which].split("]")[1]);
                } else {
                    listBatch.remove(names[which]);
                }
            }
        });
        //设置正面按钮以及点击事件（土司显示choose内容）
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VideoAsActivity.this, "选取完成", Toast.LENGTH_SHORT).show();
                initProgressDialog();
                batchIF = true;
                batchFor = 1;
                //批量下载需要需要单独使用
                cid = sj(listBatch.get(0), "[", "]");
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
                downloadVideo();
            }
        });
        builder.show();//显示Dialog对话框
    }

    //远程提交解析内容
    private void AddVideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String VideoPost = "Aid=" + URLEncoder.encode(aid) + "&Bvid=" + URLEncoder.encode(bvid) + "&Mid=" + URLEncoder.encode(Mid) + "&Upname=" + URLEncoder.encode(upName) + "&Tname=" + URLEncoder.encode(tName) + "&Copyright=" + URLEncoder.encode(Copyright);
                System.out.println(HttpUtils.doGet("https://api.misakaloli.com/bilibili/AppVideoAsAdd.php?" + VideoPost, ""));
                System.out.println(VideoJson);
            }
        }).start();
    }


    //设置下载方式
    private void setDownloadMethod() {
        AlertDialog aldg;
        AlertDialog.Builder adBd = new AlertDialog.Builder(VideoAsActivity.this);
        adBd.setTitle("设置介绍");
        String ht = "\n\n后台模式：这是直接调用系统下载管理器，是我们目前的一个新方法。但为了适配安卓7及以上，不能更改储存位置。默认储存位置/storage/emulated/0/Download/com.imcys.bilibilias/";
        String ht1 = "\n\n【特别注意】：后台下载无法使用 批量下载 音频视频分离下载，因为要重复写这个功能，偷个懒，下个版本加入。";
        adBd.setMessage("亲爱的用户，在我不断尝试下，终于解决了下载方式问题，支持了后台下载，现在请看我为您介绍的下载方式。\n\n弹窗模式：这是一种传统的下载方式，可以灵活的改变下载位置，缺点是需要一直在软件界面等待。" + ht + ht1);
        adBd.setPositiveButton("弹窗下载", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //步骤3：将获取过来的值放入文件
            editor.putString("DownloadMethod", "1");
            //步骤4：提交 commit有返回值apply没有
            editor.apply();
            DownloadMethod = sharedPreferences.getString("DownloadMethod", "");
            new download().start();
        });
        adBd.setNegativeButton("后台下载", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //步骤3：将获取过来的值放入文件
            editor.putString("DownloadMethod", "2");
            //步骤4：提交 commit有返回值apply没有
            editor.apply();
            DownloadMethod = sharedPreferences.getString("DownloadMethod", "");
            videoDownloadNotification();
        });
        aldg = adBd.create();
        aldg.show();
    }


    //下载进度对话框
    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle("下载文件");
        //获取信息
        //获取下载路径
        //DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString() + "/Path.txt");
        //DLPath = DLPath + Title + type + cid + "." + videoType;
        progressDialog.setMessage("当前下载地址\n" + DLPath + "\n进入设置页面设置下载地址");
        //设置空白处不消失
        progressDialog.setCancelable(false);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置按钮
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "暂停", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消正在下载的操作
                //结束循环
                batchIF = false;
                batchFor = 1;
                cancelable.cancel();
            }
        });


        //新建一个 ProgressBar 并设置为横向进度样式
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        DLPath = DLPath + Title + type + cid + "." + videoType;

    }


    //视频格式设置-->下拉菜单方案
    public void ListArray(List<String> arrayStrings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayStrings);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项

                String data = (String) spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                cid = sj(data, "[", "]");
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void ListArrayVideo(List<String> arrayStrings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayStrings);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String) spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                Toast.makeText(VideoAsActivity.this, data, Toast.LENGTH_SHORT).show();
                type = data;
                qn = sj(data, "[", "]");
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void ListArrayCode(List<String> arrayStrings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayStrings);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String) spinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                fnval = data;
                if (fnval.equals("flv")) {
                    videoType = "flv";
                    fnval = "0";
                    dashState = false;
                } else if (fnval.equals("mp4")) {
                    videoType = "mp4";
                    fnval = "2";
                    dashState = false;
                } else if (fnval.equals("mp4【音频视频分离下载最快】")) {
                    videoType = "mp4";
                    dashState = true;
                    fnval = "16";
                } else {
                    videoType = "flv";
                    fnval = "0";
                    dashState = false;
                }
                jxUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&type=json&fourk=1" + "&qn=" + qn + "&fnval=" + fnval;
                Log.e("下载地址", jxUrl);
                //释放弹幕内存
                if (mDanmakuView != null) {
                    // dont forget release!
                    mDanmakuView.release();
                    mDanmakuView = null;
                }
                //加载弹幕
                if (sharedPreferences.getBoolean("PlayDMSwitch", true)) {
                    GoDm();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    //取中间方法
    public String sj(String str, String start, String end) {
        if (str.contains(start) && str.contains(end)) {
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
        for (int x = 0; x < len; ) {
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


    private void GoDm() {

        new Thread(() -> {
            try {
                //获取下载路径
                String DMUrl = "http://api.bilibili.com/x/v1/dm/list.so?oid=" + cid;
                Connection.Response response = Jsoup.connect(DMUrl)
                        .method(Connection.Method.GET)
                        .timeout(5000)
                        .execute();
                BilibiliPost.fileWrite(getExternalFilesDir("哔哩哔哩视频").toString() + "/DM.xml", response.body());
                dmNew();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void dmNew() throws FileNotFoundException {
        mDanmakuView = (IDanmakuView) findViewById(R.id.As_DanmakuView);

        mContext = DanmakuContext.create();

        // 设置弹幕的最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        //maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true);

        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3) //设置描边样式
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
                .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠


        if (mDanmakuView != null) {
            InputStream input = new FileInputStream(getExternalFilesDir("哔哩哔哩视频").toString() + "/DM.xml");

            mParser = createParser(input); //创建解析器对象，从raw资源目录下解析comments.xml文本
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });

            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false); //是否显示FPS
            mDanmakuView.enableDanmakuDrawingCache(true);
        }

    }


    /**
     * 创建解析器对象，解析输入流
     *
     * @param stream
     * @return
     */
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }


    @Override
    protected void onResume() {
        StatService.onResume(this);
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() &&
                mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //弹幕内存释放
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }
}

