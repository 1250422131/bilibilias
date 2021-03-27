package com.imcys.bilibilias.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.play.PlayVideoActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserActivity extends AppCompatActivity {

    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;
    private Intent intent;
    private String toKen;
    private String csrf;
    private String cookie;
    private String face;
    private String name;
    private String money;
    private String mid;
    private String sign;
    private String sex;
    private String UserMid;
    private String topPhoto;
    private String VipText;
    private String nickname_color;
    private int level;
    private int VipType;
    private String follower;
    private String following;
    private TextView userLevel;
    private RecyclerView recyclerView;
    private UserVideoAdapter adapter;
    private EditText inputAddFreezeVideo;
    private int MRL = 1;
    private double pageCount;
    private String image_enhance;
    private String GxUrl = "https://api.misakaloli.com/app/bilibilias.php?type=json&version=1.7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //获取首页用户信息变量
        intent = getIntent();
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");
        cookie = intent.getStringExtra("cookie");


        //提示对话框
        pd2 = ProgressDialog.show(UserActivity.this, "提示", "正在拉取用户数据");
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
                    money = UserNavInfo.getString("coins");
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
                ImageView userFace = (ImageView) findViewById(R.id.User_Face);
                ImageView userEnhance = (ImageView) findViewById(R.id.User_Enhance);
                TextView nameTextView = (TextView) findViewById(R.id.User_name);
                TextView signTextView = (TextView) findViewById(R.id.User_sign);
                TextView followerTextView = (TextView) findViewById(R.id.User_follower);//粉丝
                TextView followingTextView = (TextView) findViewById(R.id.User_following);//关注
                TextView moneyTextView = (TextView) findViewById(R.id.User_money);
                nameTextView.setText(name);
                signTextView.setText(sign);
                followerTextView.setText(follower);
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
                        UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, UserActivity.this);
                        fruitList.add(VideoListData);
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
                        pd2.cancel();
                    }
                });
            }
        }).start();

    }


    //布局事件区域 ↓ ↓ ↓
    public void GoCollectionActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(UserActivity.this, CollectionActivity.class);
        intent.putExtra("cookie", cookie);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    public void GoPlaybackRecord(View view){
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
            new Thread(() -> {
                MRL = MRL + 1;
                runOnUiThread(() -> {
                    fruitList.clear();
                    adapter.notifyDataSetChanged();
                });
            }).start();
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
                        UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, UserActivity.this);
                        fruitList.add(VideoListData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    recyclerView = findViewById(R.id.User_RecyclerView);
                    GridLayoutManager layoutManager = new GridLayoutManager(UserActivity.this, 2);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new UserVideoAdapter(fruitList);
                    recyclerView.setAdapter(adapter);
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
            String AddVideo = HttpUtils.doPost("https://api.misakaloli.com/app/AppFunction.php?type=FVideoAdd", "token=" + startTs * 6 + "&mid=" + mid + "&Bvid=" + inputAddFreezeVideo.getText().toString(), "");
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
            String AddVideo = HttpUtils.doPost("https://api.misakaloli.com/app/AppFunction.php?type=FUpAdd", "token=" + startTs * 6 + "&mid=" + mid, "");
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
        Toast.makeText(UserActivity.this, "正在开发", Toast.LENGTH_SHORT).show();
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