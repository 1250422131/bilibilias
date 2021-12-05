package com.imcys.bilibilias.user;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;

import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.home.VerificationUtils;
import com.imcys.bilibilias.play.PlayVideoActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.samlss.broccoli.Broccoli;
import me.samlss.broccoli.BroccoliGradientDrawable;
import me.samlss.broccoli.PlaceholderParameter;


public class UserActivity extends AppCompatActivity {

    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;
    private Intent intent;
    private String toKen;
    private String csrf;
    public static String cookie;
    private String face;
    private String name;
    private String money;
    public static String mid;
    private String sign;
    private String topPhoto;
    private String VipText;
    private String nickname_color;
    private int level;
    private int VipType;
    private String follower;
    private String following;
    private RecyclerView recyclerView;
    private UserVideoAdapter adapter;
    private EditText inputAddFreezeVideo;
    private int MRL = 1;
    private double pageCount;
    private String image_enhance;
    private String GxUrl = "https://api.misakamoe.com/app/bilibilias.php?type=json&version=2.0";
    Broccoli broccoli;
    private View content;
    private int mX;
    private int mY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        //获取首页用户信息变量
        intent = getIntent();
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");
        String goUP = intent.getStringExtra("UP");



        //占位加载
        ImageView userFace = (ImageView) findViewById(R.id.User_Face);
        ImageView userEnhance = (ImageView) findViewById(R.id.User_Enhance);
        TextView nameTextView = (TextView) findViewById(R.id.User_name);
        TextView signTextView = (TextView) findViewById(R.id.User_sign);
        TextView followerTextView = (TextView) findViewById(R.id.User_follower);//粉丝
        TextView followingTextView = (TextView) findViewById(R.id.User_following);//关注
        TextView moneyTextView = (TextView) findViewById(R.id.User_money);
        LinearLayout levelCard = (LinearLayout) findViewById(R.id.User_LevelLinearLayout);
        LinearLayout vipCard = (LinearLayout) findViewById(R.id.User_VipLinearLayout);

        broccoli = new Broccoli();
        View listView[] = {userFace, nameTextView, signTextView, followerTextView, followingTextView, moneyTextView, levelCard, vipCard};
        for (int i = 0; i < listView.length; i++) {
            broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                    .setView(listView[i])
                    .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                            Color.parseColor("#CCCCCC"), 0, 1000, new LinearInterpolator()))
                    .build());
        }
        broccoli.show();


        //加载用户数据
        new Thread((Runnable) () -> {
            String UserInfo = null;
            UserInfo = HttpUtils.doGet("https://api.bilibili.com/x/space/acc/info?mid=" + mid, cookie);
            final String UserFansJson = HttpUtils.doGet("https://api.bilibili.com/x/relation/stat?vmid=" + mid, cookie);
            final String finalUserInfo = UserInfo;
            runOnUiThread((Runnable) () -> {
                try {
                    //个人中心内容填充
                    JSONObject UserFansData = new JSONObject(UserFansJson);
                    UserFansData = UserFansData.getJSONObject("data");
                    follower = UserFansData.getString("follower");
                    following = UserFansData.getString("following");
                    JSONObject UserNavInfo = new JSONObject(finalUserInfo);
                    UserNavInfo = UserNavInfo.getJSONObject("data");
                    JSONObject UserPendant = UserNavInfo.getJSONObject("pendant");
                    image_enhance = UserPendant.getString("image_enhance");
                    JSONObject UserVipData = UserNavInfo.getJSONObject("vip");
                    VipType = UserVipData.getInt("type");
                    topPhoto = UserNavInfo.getString("top_photo");
                    level = UserNavInfo.getInt("level");
                    face = UserNavInfo.getString("face");
                    name = UserNavInfo.getString("name");
                    if (goUP == null) {
                        money = UserNavInfo.getString("coins");
                    } else {
                        money = "";
                        androidx.appcompat.widget.Toolbar mToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.User_Toolbar);
                        mToolbar.setTitle("UP主页");
                        TextView videoText = (TextView) findViewById(R.id.User_VideoText);
                        videoText.setText("UP的投稿");
                        LinearLayout moneyLy = (LinearLayout) findViewById(R.id.User_money_LinearLayout);
                        moneyLy.setVisibility(View.GONE);
                        CardView UserCardView = (CardView) findViewById(R.id.User_Fusion_CardView);
                        UserCardView.setVisibility(View.GONE);
                    }
                    sign = UserNavInfo.getString("sign");
                    if (VipType != 0) {
                        nickname_color = UserVipData.getString("nickname_color");
                        UserVipData = UserVipData.getJSONObject("label");
                        VipText = UserVipData.getString("text");
                    } else {
                        nickname_color = "";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LinearLayout UserVipLinearLayout = (LinearLayout) findViewById(R.id.Home_FunctionRecyclerView);
                TextView userLevel = (TextView) findViewById(R.id.User_Level);
                TextView userVip = (TextView) findViewById(R.id.User_Vip);

                nameTextView.setText(name);
                signTextView.setText(sign);
                followerTextView.setText(VerificationUtils.DigitalConversion(Integer.parseInt(follower)));
                followingTextView.setText(following);
                moneyTextView.setText(money);
                if (!image_enhance.equals("")) {
                    Glide.with(UserActivity.this).load(image_enhance).into(userEnhance);
                } else {
                    userEnhance.setVisibility(View.GONE);
                }
                Glide.with(UserActivity.this)
                        .load(face)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(userFace);
                userLevel.setText(" LV" + level + " ");
                if (VipType != 0 & !nickname_color.equals("")) {
                    nameTextView.setTextColor(Color.parseColor(nickname_color));
                    userVip.setText(" " + VipText + " ");
                } else {
                    userVip.setVisibility(View.GONE);
                }

            });
        }).start();


        //加载结束
        //视频列表加载事件
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserVideoStr = HttpUtils.doGet("https://api.bilibili.com/x/space/arc/search?mid=" + mid + "&pn=1&ps=25&order=pubdate&index=1&jsonp=jsonp", cookie);
                try {
                    JSONObject UserVideoJson = new JSONObject(UserVideoStr);
                    UserVideoJson = UserVideoJson.getJSONObject("data");
                    JSONObject UserPage = UserVideoJson.getJSONObject("page");
                    pageCount = UserPage.getDouble("count");
                    pageCount = Math.ceil(pageCount / 20);
                    UserVideoJson = UserVideoJson.getJSONObject("list");
                    JSONArray UserVideoArray = UserVideoJson.getJSONArray("vlist");
                    for (int i = 0; i < UserVideoArray.length(); i++) {
                        JSONObject UserVideoData = UserVideoArray.getJSONObject(i);
                        String Title = UserVideoData.getString("title");
                        String pic = UserVideoData.getString("pic");
                        String play = UserVideoData.getString("play");
                        String Dm = UserVideoData.getString("video_review");
                        String bvid = UserVideoData.getString("bvid");
                        String aid = UserVideoData.getString("aid");
                        try {
                            int playInt = Integer.parseInt(play);
                            int DmInt = Integer.parseInt(Dm);
                            play = VerificationUtils.DigitalConversion(playInt);
                            Dm = VerificationUtils.DigitalConversion(DmInt);
                            UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, UserActivity.this);
                            fruitList.add(VideoListData);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //执行刷新
                        recyclerView = (RecyclerView) findViewById(R.id.User_RecyclerView);
                        GridLayoutManager layoutManager = new GridLayoutManager(UserActivity.this, 2);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new UserVideoAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (isSlideToBottom(recyclerView)) {
                                    Snackbar.make(findViewById(R.id.User_CoordinatorLayout), "呜哇，我是有底线的", Snackbar.LENGTH_LONG)
                                            .setAction("加载下一页", v -> SlideToBottom()).show();
                                }
                            }
                        });

                        broccoli.clearAllPlaceholders();
                    }
                });
            }
        }).start();

    }


    // 动画
    private Animator createRevealAnimator(boolean reversed, int x, int y) {
        float hypot = (float) Math.hypot(content.getHeight(), content.getWidth());
        float startRadius = reversed ? hypot : 0;
        float endRadius = reversed ? 0 : hypot;

        Animator animator = ViewAnimationUtils.createCircularReveal(
                content, x, y,
                startRadius,
                endRadius);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }


    //布局事件区域 ↓ ↓ ↓
    public void GoCollectionActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(UserActivity.this, CollectionActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    public void GoPlaybackRecord(View view) {
        Intent intent = new Intent();
        intent.setClass(UserActivity.this, PlaybackRecordActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }


    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


    private void SlideToBottom() {
        if (MRL < (int) pageCount) {
            MRL = MRL + 1;
            new Thread((Runnable) () -> {
                String UserVideoStr = HttpUtils.doGet("https://api.bilibili.com/x/space/arc/search?mid=" + mid + "&pn=" + MRL + "&ps=25&order=pubdate&index=1&jsonp=jsonp", cookie);
                try {
                    JSONObject UserVideoJson = new JSONObject(UserVideoStr);
                    UserVideoJson = UserVideoJson.getJSONObject("data");
                    JSONObject UserPage = UserVideoJson.getJSONObject("page");
                    pageCount = UserPage.getDouble("count");
                    pageCount = Math.ceil(pageCount / 25);
                    UserVideoJson = UserVideoJson.getJSONObject("list");
                    JSONArray UserVideoArray = UserVideoJson.getJSONArray("vlist");
                    for (int i = 0; i < UserVideoArray.length(); i++) {
                        JSONObject UserVideoData = UserVideoArray.getJSONObject(i);
                        String Title = UserVideoData.getString("title");
                        String pic = UserVideoData.getString("pic");
                        String play = UserVideoData.getString("play");
                        String Dm = UserVideoData.getString("video_review");
                        String bvid = UserVideoData.getString("bvid");
                        String aid = UserVideoData.getString("aid");
                        try {
                            int playInt = Integer.parseInt(play);
                            int DmInt = Integer.parseInt(Dm);
                            play = VerificationUtils.DigitalConversion(playInt);
                            Dm = VerificationUtils.DigitalConversion(DmInt);
                            UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, UserActivity.this);
                            fruitList.add(VideoListData);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            }).start();
        } else {
            Snackbar.make(findViewById(R.id.User_CoordinatorLayout), "o(´^｀)o这次真到底啦", Snackbar.LENGTH_SHORT).show();
        }
    }


    public void addFreeze(View view) {
        AlertDialog aldg;
        AlertDialog.Builder adBd = new AlertDialog.Builder(UserActivity.this);
        adBd.setTitle("温馨提示");
        adBd.setMessage("亲爱的UP主，我了解你现在的心情，但在冻结之前，请认真考虑冻结模式");
        adBd.setPositiveButton("冻结单个视频", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputAddFreezeVideo = new EditText(UserActivity.this);
                inputAddFreezeVideo.setHint("输入完整BV号");
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setView(inputAddFreezeVideo);
                builder.setTitle("冻结视频")
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AddFreezeVideo();
                    }
                });
                builder.show();
            }
        });
        adBd.setNegativeButton("冻结全体视频", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddFreezeUp();
            }
        });
        adBd.setNeutralButton("取消", null);
        aldg = adBd.create();
        aldg.show();
    }


    private void AddFreezeVideo() {
        pd2 = ProgressDialog.show(UserActivity.this, "提示", "正在提交");
        new Thread(() -> {
            Long startTs = System.currentTimeMillis(); // 当前时间戳
            String AddVideo = HttpUtils.doPost("https://api.misakamoe.com/app/AppFunction.php?type=FVideoAdd", "token=" + startTs * 6 + "&mid=" + mid + "&Bvid=" + inputAddFreezeVideo.getText().toString(), "");
            try {
                JSONObject AddVideoJson = new JSONObject(AddVideo);
                String msg = AddVideoJson.getString("msg");
                runOnUiThread(() -> {
                    Toast.makeText(UserActivity.this, msg, Toast.LENGTH_SHORT).show();
                    pd2.cancel();
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void AddFreezeUp() {
        pd2 = ProgressDialog.show(UserActivity.this, "提示", "正在提交");
        new Thread(() -> {
            Long startTs = System.currentTimeMillis(); // 当前时间戳
            String AddVideo = HttpUtils.doPost("https://api.misakamoe.com/app/AppFunction.php?type=FUpAdd", "token=" + startTs * 6 + "&mid=" + mid, "");
            System.out.println(AddVideo);
            try {
                JSONObject AddVideoJson = new JSONObject(AddVideo);
                String msg = AddVideoJson.getString("msg");
                runOnUiThread(() -> {
                    Toast.makeText(UserActivity.this, msg, Toast.LENGTH_SHORT).show();
                    pd2.cancel();
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void getNot(View view) {
        Intent intent = new Intent();
        intent.setClass(UserActivity.this, CreativeCenterActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    public void getCache(View view) {
        Intent intent = new Intent();
        intent.setClass(UserActivity.this, CacheActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }


    public void Notice(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String StrGx = HttpUtils.doGet(GxUrl, cookie);
                try {
                    JSONObject jsonGxStr = new JSONObject(StrGx);
                    final String Gg = jsonGxStr.getString("notice");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(UserActivity.this)
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
    }


}