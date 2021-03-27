package com.imcys.bilibilias.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.imcys.bilibilias.AppFilePathUtils;
import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.MergeVideoActivity;
import com.imcys.bilibilias.as.RankingActivity;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.imcys.bilibilias.play.PlayPathActivity;
import com.imcys.bilibilias.user.AboutActivity;
import com.imcys.bilibilias.user.UserActivity;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NewHomeActivity extends AppCompatActivity {

    private List<Function> functionList = new ArrayList<>();
    private Function FunctionListData;
    private GridLayoutManager layoutManager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Banner banner;

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
    private String GxUrl = "https://api.misakaloli.com/app/bilibilias.php?type=json&version=1.7";
    private String Version = "1.7";
    private AppFilePathUtils mAppFilePathUtils;
    private SharedPreferences sharedPreferences;
    private String ps = "如果你正在逆向，不如联系作者QQ1250422131 我会给你想要的东西";
    private static final int NO_1 = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhome);
        //权限获取
        checkPermission();
        //实例化常用控件
        newView();
        //加载首页内容
        initDrawerToggle();
        //正版查询 -> 检测布局是否正常
        getApkMd5();
        //检查是否同意隐私政策
        newVersionCheck();
        //检测下载配置
        String DLPath = getExternalFilesDir("下载设置").toString() + "/Path.txt";
        File file = new File(DLPath);
        if (!file.exists()) {
            try {
                BilibiliPost.fileWrite(DLPath, getExternalFilesDir("哔哩哔哩视频").toString() + "/");
                DLPath = getExternalFilesDir("下载设置").toString() + "/断点续传设置.txt";
                file = new File(DLPath);
                if (!file.exists()) {
                    BilibiliPost.fileWrite(DLPath, "1");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getCacheSize();
        //轮播图加载
        setBanner();
        //百度统计执行
        StatService.setAuthorizedState(NewHomeActivity.this, true);
        StatService.start(this);
    }


    private void newVersionCheck() {
        //步骤1：创建一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String SPVersion = sharedPreferences.getString("Version", "");
        if (!SPVersion.equals(Version)) { //Version
            //步骤2： 实例化SharedPreferences.Editor对象
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //步骤3：将获取过来的值放入文件
            editor.putString("Version", "1.7");
            //步骤4：提交 commit有返回值apply没有
            editor.apply();
            LinearLayout lLayout = new LinearLayout(this);
            lLayout.setOrientation(LinearLayout.VERTICAL);
            WebView Privacy = new WebView(NewHomeActivity.this);
            Privacy.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
            Privacy.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
            Privacy.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
            Privacy.getSettings().setDomStorageEnabled(true);//DOM Storage 重点是设置这个
            Privacy.getSettings().setAllowFileAccess(false);
            Privacy.loadUrl("https://docs.qq.com/doc/DVWdlb2hSWFlJaUFk");
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1500);//这个属性是设置空间的长宽，其实还可以设置其他的控件的其他属性；
            lLayout.addView(Privacy, lParams);
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NewHomeActivity.this);
            builder.setView(lLayout);
            builder.setCancelable(false);
            builder.setTitle("隐私政策");
            builder.setPositiveButton("使用程序则代表同意协议", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(NewHomeActivity.this, VersionActivity.class);
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }

    private void getCacheSize() {
        mAppFilePathUtils = new AppFilePathUtils(NewHomeActivity.this, "com.imcys.bilibilias");
        final long total = AppFilePathUtils.getTotalSizeOfFilesInDir(mAppFilePathUtils.getCache());
        double cacheNc = (double) (total / 1048576);
        if ((int) cacheNc > 60) {
            androidx.appcompat.app.AlertDialog aldg;
            androidx.appcompat.app.AlertDialog.Builder adBd = new androidx.appcompat.app.AlertDialog.Builder(NewHomeActivity.this);
            adBd.setTitle("缓存过多");
            adBd.setMessage("那个那个，因为程序运行，造成了一定缓存，并且超过了60MB，这些并不是下载的视频文件，而是使用中产生的缓存垃圾，请选择");
            adBd.setPositiveButton("清理缓存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppFilePathUtils.deleteDir(getCacheDir());
                    Toast.makeText(NewHomeActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                }
            });
            adBd.setNegativeButton("我内存大", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            aldg = adBd.create();
            aldg.show();
        }
    }

    //实例化全体控件
    private void newView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Home_FunctionRecyclerView);
        drawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
    }

    //侧滑和首页的展示
    private void initDrawerToggle() {
        toolbar = (Toolbar) findViewById(R.id.Home_Toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setSubtitle("没有未来的未来，不是我想要的未来");
        toolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        //设置返回键生效
        getSupportActionBar().setHomeButtonEnabled(true);
        // 参数：开启抽屉的activity、DrawerLayout的对象、toolbar按钮打开关闭的对象、描述open drawer、描述close drawer
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        // 添加抽屉按钮，通过点击按钮实现打开和关闭功能; 如果不想要抽屉按钮，只允许在侧边边界拉出侧边栏，可以不写此行代码
        mDrawerToggle.syncState();
        // 设置按钮的动画效果; 如果不想要打开关闭抽屉时的箭头动画效果，可以不写此行代码
        drawerLayout.setDrawerListener(mDrawerToggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示侧滑菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String HomeItemStr = HttpUtils.doGet("https://api.misakaloli.com/app/AppFunction.php?type=homeitem", "");
                try {
                    JSONObject HomeItemJson = new JSONObject(HomeItemStr);
                    JSONArray HomeItemArray = HomeItemJson.getJSONArray("data");
                    for (int i = 0; i < HomeItemArray.length(); i++) {
                        JSONObject HomeItemData = HomeItemArray.getJSONObject(i);
                        String Title = HomeItemData.getString("Title");
                        String SrcUrl = HomeItemData.getString("SrcUrl");
                        int ViewTag = HomeItemData.getInt("ViewTag");
                        int visibility = HomeItemData.getInt("visibility");
                        if (visibility == 1) {
                            FunctionListData = new Function(Title, SrcUrl, ViewTag, NewHomeActivity.this);
                            functionList.add(FunctionListData);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Home_FunctionRecyclerView);
                        GridLayoutManager layoutManager = new GridLayoutManager(NewHomeActivity.this, 2);
                        recyclerView.setLayoutManager(layoutManager);
                        FunctionAdapter adapter = new FunctionAdapter(functionList);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new FunctionAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(int position, int tag) {
                                switch (tag) {
                                    case 1:
                                        GoUser();
                                        break;
                                    case 2:
                                        GoVideoAs();
                                        break;
                                    case 3:
                                        goPlayPath();
                                        break;
                                    case 4:
                                        goRanking();
                                        break;
                                    case 5:
                                        goSet();
                                        break;
                                    case 6:
                                        UserExit();
                                        break;
                                    case 7:
                                        goMergeVideo();
                                        break;
                                    default:
                                        Toast.makeText(NewHomeActivity.this, "内测功能，请等待新版本更新。", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }


    //布局事件区域 ↓ ↓ ↓
    private void GoUser() {
        Intent intent = new Intent();
        intent.setClass(NewHomeActivity.this, UserActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("csrf", csrf);
        intent.putExtra("toKen", toKen);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    private void GoVideoAs() {
        Intent intent = new Intent();
        intent.setClass(NewHomeActivity.this, VideoAsActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("csrf", csrf);
        intent.putExtra("toKen", toKen);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    private void goSet() {
        Intent intent = new Intent();
        intent.setClass(NewHomeActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void goRanking() {
        Intent intent = new Intent();
        intent.setClass(NewHomeActivity.this, RankingActivity.class);
        startActivity(intent);
    }

    private void goPlayPath() {
        Intent intent = new Intent();
        intent.setClass(NewHomeActivity.this, PlayPathActivity.class);
        startActivity(intent);
    }

    private void goMergeVideo(){
        Intent intent = new Intent();
        intent.setClass(NewHomeActivity.this, MergeVideoActivity.class);
        startActivity(intent);
    }

    private void UserExit() {
        androidx.appcompat.app.AlertDialog aldg;
        androidx.appcompat.app.AlertDialog.Builder adBd = new androidx.appcompat.app.AlertDialog.Builder(NewHomeActivity.this);
        adBd.setTitle("警告");
        adBd.setMessage("awa，那个那个，要离开本程序同时清除B站服务器的登录身份记录吗？");
        adBd.setPositiveButton("嗯嗯，郑要走惹", (dialog, which) -> new Thread(() -> {
            HttpUtils.doPost("https://passport.bilibili.com/login/exit/v2", "biliCSRF=" + csrf, cookie);
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(), "退出完成", Toast.LENGTH_SHORT).show();
                System.exit(0);
            });
        }).start());
        adBd.setNegativeButton("手滑惹", (dialog, which) -> {
        });
        aldg = adBd.create();
        aldg.show();
    }


    //正版鉴权↓ ↓ ↓
    private void getApkMd5() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String StrGx = HttpUtils.doGet(GxUrl, "");
                try {
                    JSONObject jsonGxStr = new JSONObject(StrGx);
                    final String MD5 = jsonGxStr.getString("APKMD5");
                    final String CRC = jsonGxStr.getString("APKToKenCR");
                    final String SHA = jsonGxStr.getString("APKToKen");
                    final String ID = jsonGxStr.getString("ID");
                    final String StrUrl = jsonGxStr.getString("url");
                    final String sha = apkVerifyWithSHA(NewHomeActivity.this, SHA);
                    System.out.println(sha);
                    System.out.println(SHA);
                    final String md5 = apkVerifyWithMD5(NewHomeActivity.this, MD5);
                    System.out.println(md5);
                    System.out.println(MD5);
                    final String crc = apkVerifyWithCRC(NewHomeActivity.this, CRC);
                    System.out.println(crc);
                    System.out.println(CRC);
                    if (ID.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!sha.equals(SHA)) {
                                    DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
                                    mDrawerLayout.setVisibility(View.GONE);
                                    Uri uri = Uri.parse(StrUrl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    System.exit(0);
                                } else if (!md5.equals(MD5)) {
                                    DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
                                    mDrawerLayout.setVisibility(View.GONE);
                                    Uri uri = Uri.parse(StrUrl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    System.exit(0);
                                } else if (!crc.equals(CRC)) {
                                    DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
                                    mDrawerLayout.setVisibility(View.GONE);
                                    Uri uri = Uri.parse(StrUrl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    System.exit(0);
                                }
                            }
                        });
                    } else if (ID.equals("0")) {
                        String fs = HttpUtils.doGet("https://api.misakaloli.com/app/bilibilias.php?type=json&version=" + Version + "&SHA=" + sha + "&MD5=" + md5 + "&CRC=" + crc + "lj=" + LJ, cookie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //数据更新
        LoginCheck();
    }

    private void getPirate() {
        banner = (Banner) findViewById(R.id.Home_banner);
        RelativeLayout mRelativeLayout = findViewById(R.id.Home_Banner_RelativeLayout);
        CardView mCardView = findViewById(R.id.Home_Banner_CardView);
        LinearLayout mLinearLayout = findViewById(R.id.Home_MainNewLinearLayout);
        AppBarLayout mAppBarLayout = findViewById(R.id.Home_AppBarLayout);
        if (banner.getVisibility() == View.GONE) {
            goWebUrl();
        } else if (mRelativeLayout.getVisibility() == View.GONE) {
            goWebUrl();
        } else if (mCardView.getVisibility() == View.GONE) {
            goWebUrl();
        } else if (mLinearLayout.getVisibility() == View.GONE) {
            goWebUrl();
        } else if (mAppBarLayout.getVisibility() == View.GONE) {
            goWebUrl();
        }
    }

    private void goWebUrl() {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
        mDrawerLayout.setVisibility(View.GONE);
        Uri uri = Uri.parse("https//:api.misakaloli.bilibilias/app");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        System.exit(0);
    }


    private void getNotice() {
        //更新检测
        new Thread(new Runnable() {
            @Override
            public void run() {
                String StrGx = HttpUtils.doGet(GxUrl, "");
                if (!GxUrl.equals("https://api.misakaloli.com/app/bilibilias.php?type=json&version=" + Version)) {
                } else {
                    JSONObject jsonGxStr = null;
                    String StrPd = null;
                    try {
                        jsonGxStr = new JSONObject(StrGx);
                        StrPd = jsonGxStr.getString("version");
                        final String StrNr = jsonGxStr.getString("gxnotice");
                        final String StrUrl = jsonGxStr.getString("url");
                        if (StrPd.equals("1.6")) {
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog dialog = new AlertDialog.Builder(NewHomeActivity.this)
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

    }


    private void setBanner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String bannerJsonStr = HttpUtils.doGet("https://api.misakaloli.com/app/bilibilias.php?type=banner", "");
                try {
                    JSONObject bannerJson = new JSONObject(bannerJsonStr);
                    JSONArray imgUrlList = bannerJson.getJSONArray("imgUrlList");
                    JSONArray textList = bannerJson.getJSONArray("textList");
                    JSONArray dataList = bannerJson.getJSONArray("dataList");
                    JSONArray typeList = bannerJson.getJSONArray("typeList");
                    JSONArray successToastList = bannerJson.getJSONArray("successToast");
                    JSONArray failToastList = bannerJson.getJSONArray("failToast");
                    JSONArray postDataList = bannerJson.getJSONArray("postData");
                    JSONArray tokenList = bannerJson.getJSONArray("token");
                    int bannerTime = bannerJson.getInt("time");
                    ArrayList<String> imagesArray = new ArrayList<>();
                    ArrayList<String> titleArray = new ArrayList<>();
                    for (int i = 0; i < imgUrlList.length(); i++) {
                        String imgUrl = imgUrlList.getString(i);
                        String title = textList.getString(i);
                        imagesArray.add(imgUrl);
                        titleArray.add(title);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            banner = (Banner) findViewById(R.id.Home_banner);
                            //设置图片加载器
                            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                            banner.setBannerTitles(titleArray);
                            banner.isAutoPlay(true);
                            banner.setDelayTime(bannerTime);
                            banner.setImageLoader(new NewHomeActivity.GlideImageLoader());
                            //设置图片集合
                            banner.setImages(imagesArray);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                            banner.setOnBannerListener(new OnBannerListener() {
                                @SuppressLint("IntentReset")
                                @Override
                                public void OnBannerClick(int position) {
                                    try {
                                        String data = dataList.getString(position);
                                        String type = typeList.getString(position);
                                        if (type.equals("goBilibili")) {
                                            Intent intent = new Intent();
                                            intent.setType("text/plain");
                                            intent.setData(Uri.parse(data));
                                            intent.setAction("android.intent.action.VIEW");
                                            startActivity(intent);
                                        } else if (type.equals("goAs")) {
                                            Intent intent = new Intent();
                                            intent.setClass(NewHomeActivity.this, VideoAsActivity.class);
                                            intent.putExtra("UserVideoAid", data);
                                            NewHomeActivity.this.startActivity(intent);
                                        } else if (type.equals("goUrl")) {
                                            Uri uri = Uri.parse(data);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        } else if (type.equals("getBiliBili")) {
                                            String successToast = successToastList.getString(position);
                                            String failToast = failToastList.getString(position);
                                            getBiliBili(data, successToast, failToast);
                                        } else if (type.equals("postBiliBili")) {
                                            String successToast = successToastList.getString(position);
                                            String failToast = failToastList.getString(position);
                                            String postData = postDataList.getString(position);
                                            int token = tokenList.getInt(position);
                                            postBiliBili(data, postData, successToast, failToast, token);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private void getBiliBili(String goUrl, String successToast, String failToast) {
        new Thread(() -> {
            String goUrlStr = HttpUtils.doGet(goUrl, cookie);
            try {
                JSONObject goUrlJson = new JSONObject(goUrlStr);
                int code = goUrlJson.getInt("code");
                runOnUiThread(() -> {
                    if (code == 0) {
                        Toast.makeText(NewHomeActivity.this, successToast, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewHomeActivity.this, failToast, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void postBiliBili(String goUrl, String post, String successToast, String failToast, int getToken) {
        new Thread(() -> {
            String getPost = post;
            if (getToken == 1) {
                getPost = post.replace("{token}", csrf);
                System.out.println(getPost);
            }
            String goUrlStr = HttpUtils.doPost(goUrl, getPost, cookie);
            try {
                JSONObject goUrlJson = new JSONObject(goUrlStr);
                int code = goUrlJson.getInt("code");
                runOnUiThread(() -> {
                    if (code == 0) {
                        Toast.makeText(NewHomeActivity.this, successToast, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewHomeActivity.this, failToast, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }


    //图片加载
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
        }
    }


    //用户登录情况检测
    private void LoginCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取必要信息内容

                final String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "token.txt";
                String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "csrf.txt";
                String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "cookie.txt";
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl", "");
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
                if (pd.equals("0")) {
                    final String UserNavStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/nav", cookie);
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
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init("https://api.misakaloli.com/app/BiliBiliAsLogin.php?URL=" + URL);
                            WebView webView1 = (WebView) findViewById(R.id.Home_WebView1);
                            webView1.setWebChromeClient(new WebChromeClient());
                            webView1.setWebViewClient(new NewHomeActivity.NewWebViewClient());
                            ScrollView webLayout = (ScrollView) findViewById(R.id.Home_WebLayout);
                            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
                            webLayout.setVisibility(View.VISIBLE);
                            mDrawerLayout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    //用户登录事件
    private void init(String LoginUrl) {
        WebView webView1 = (WebView) findViewById(R.id.Home_WebView1);
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

    public void NewLogin(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl", "");
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
                        String QRUrl = "https://api.qrserver.com/v1/create-qr-code/?data=" + URL;
                        URL = "https://api.misakaloli.com/app/BiliBiliAsLogin.php?URL=" + URL;
                        init(URL);
                        URL = QRUrl;
                        WebView webView1 = (WebView) findViewById(R.id.Home_WebView1);
                        webView1.setWebChromeClient(new WebChromeClient());
                        webView1.setWebViewClient(new NewHomeActivity.NewWebViewClient());
                        ScrollView webLayout = (ScrollView) findViewById(R.id.Home_WebLayout);
                        webLayout.setVisibility(View.VISIBLE);
                        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
                        mDrawerLayout.setVisibility(View.GONE);
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
                    cookie = HttpUtils.getCookie("oauthKey=" + oauthKey, "http://passport.bilibili.com/qrcode/getLoginInfo");
                    System.out.println(cookie);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cookie.length() > 45) {
                    final String UserNavStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/nav", cookie);
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
                            toKen = "SESSDATA=" + sj(cookie, "SESSDATA=", ";");
                            csrf = sj(cookie, "bili_jct=", ";");
                            System.out.println(toKen);
                            String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "cookie.txt";
                            String ToKenPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "token.txt";
                            String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString() + "/" + "csrf.txt";
                            try {
                                BilibiliPost.fileWrite(CookiePath, cookie);
                                BilibiliPost.fileWrite(ToKenPath, toKen);
                                BilibiliPost.fileWrite(csrfPath, csrf);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ScrollView webLayout = (ScrollView) findViewById(R.id.Home_WebLayout);
                            webLayout.setVisibility(View.GONE);
                            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.Home_DrawerLayout);
                            mDrawerLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
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
                String likeStr = HttpUtils.doGet("http://passport.bilibili.com/qrcode/getLoginUrl", "");
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
                        String QRUrl = "https://api.qrserver.com/v1/create-qr-code/?data=" + URL;
                        URL = "https://api.misakaloli.com/app/BiliBiliAsLogin.php?URL=" + URL;
                        init(URL);
                        URL = QRUrl;
                        WebView webView1 = (WebView) findViewById(R.id.Home_WebView1);
                        webView1.setWebChromeClient(new WebChromeClient());
                        webView1.setWebViewClient(new NewHomeActivity.NewWebViewClient());
                        ScrollView webLayout = (ScrollView) findViewById(R.id.Home_WebLayout);
                        webLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }


    public void QRDownload(View view) {
        //二维码下载
        pd2 = ProgressDialog.show(NewHomeActivity.this, "提示", "正在拉取数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap QRBitmap = BilibiliPost.returnBitMap("https://api.qrserver.com/v1/create-qr-code/?data=" + URL);
                savePhoto(NewHomeActivity.this, QRBitmap);
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

    private void SetQR() {
        try {
            Context context = NewHomeActivity.this;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"));
            PackageManager packageManager = context.getPackageManager();
            ComponentName componentName = intent.resolveActivity(packageManager);
            if (componentName != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "呜哇，好像没有安装B站", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转B站扫码入口
    public void SetQR(View view) {
        SetQR();
    }

    //图片保存方案
    //保存到本地
    public static void savePhoto(Context context, Bitmap bitmap) {
        File photoDir = new File(Environment.getExternalStorageDirectory(), "BILIBILIAS");
        if (!photoDir.exists()) {
            photoDir.mkdirs();
        }
        String fileName = "BILIBILIAS扫码.jpg";
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


    //字符串切割取中间
    public static String sj(String str, String start, String end) {
        if (str.contains(start) && str.contains(end)) {
            str = str.substring(str.indexOf(start) + start.length());
            return str.substring(0, str.indexOf(end));
        }
        return "";
    }


    /**
     * 获取通知权限
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
            ActivityCompat.requestPermissions(NewHomeActivity.this, permissions, PERMISSION_REQUEST);
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
    public static String apkVerifyWithMD5(Context context, String orginalMD5) {
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