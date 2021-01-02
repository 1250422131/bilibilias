package com.imcys.bilibilias.user;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.tencent.bugly.crashreport.CrashReport;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class UserActivity extends AppCompatActivity {

    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;
    private Intent intent ;
    private String toKen ;
    private String csrf  ;
    private String cookie ;
    private String face;
    private String name;
    private String money;
    private String mid;
    private String sign;
    private String sex;
    private String UserMid;
    private String follower;
    private String following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("个人中心");

        //获取首页用户信息变量
        intent = getIntent();
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");

        //BUGly
        CrashReport.initCrashReport(getApplicationContext(), "1bb190bc7d", false);


        //提示对话框
        pd2 = ProgressDialog.show(UserActivity.this, "提示", "正在拉取用户数据");
        //加载用户数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserInfo = null;
                UserInfo = HttpUtils.doGet("https://api.bilibili.com/x/space/acc/info?mid="+mid,cookie);
                final String UserFansJson = HttpUtils.doGet("https://api.bilibili.com/x/relation/stat?vmid="+mid,cookie);
                final String finalUserInfo = UserInfo;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //个人中心内容填充
                            JSONObject UserFansData = new JSONObject(UserFansJson);
                            UserFansData = UserFansData.getJSONObject("data");
                            follower = UserFansData.getString("follower");
                            following = UserFansData.getString("following");
                            JSONObject UserNavInfo = new JSONObject(finalUserInfo);
                            UserNavInfo = UserNavInfo.getJSONObject("data");
                            face = UserNavInfo.getString("face");
                            name = UserNavInfo.getString("name");
                            money = UserNavInfo.getString("coins");
                            sign = UserNavInfo.getString("sign");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ImageView userFace = (ImageView)findViewById(R.id.User_Face);
                        TextView nameTextView = (TextView)findViewById(R.id.User_name);
                        TextView signTextView = (TextView)findViewById(R.id.User_sign);
                        TextView followerTextView = (TextView)findViewById(R.id.User_follower);//粉丝
                        TextView followingTextView = (TextView)findViewById(R.id.User_following);//关注
                        TextView midTextView = (TextView)findViewById(R.id.User_mid);
                        TextView moneyTextView = (TextView)findViewById(R.id.User_money);
                        nameTextView.setText(name);
                        signTextView.setText(sign);
                        followerTextView.setText(follower);
                        followingTextView.setText(following);
                        midTextView.setText(mid);
                        moneyTextView.setText(money);
                        Glide.with(UserActivity.this)
                                .load(face)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(userFace);
                    }
                });
            }
        }).start();


        //加载结束
        //视频列表加载事件
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserVideoStr = HttpUtils.doGet("https://api.bilibili.com/x/space/arc/search?mid="+mid,cookie);
                try {
                    JSONObject UserVideoJson = new JSONObject(UserVideoStr);
                    UserVideoJson = UserVideoJson.getJSONObject("data");
                    UserVideoJson = UserVideoJson.getJSONObject("list");
                    JSONArray UserVideoArray = UserVideoJson.getJSONArray("vlist");
                    for (int i = 0; i<UserVideoArray.length();i++){
                        JSONObject UserVideoData = UserVideoArray.getJSONObject(i);
                        String Title = UserVideoData.getString("title");
                        String pic = UserVideoData.getString("pic");
                        String play = UserVideoData.getString("play");
                        String Dm = UserVideoData.getString("video_review");
                        String bvid = UserVideoData.getString("bvid");
                        String aid = UserVideoData.getString("aid");
                        UserVideo VideoListData = new UserVideo(Title,bvid,aid,"https://"+pic,play,Dm,UserActivity.this);
                        fruitList.add(VideoListData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd2.cancel();
                    }
                });
            }
        }).start();

        //执行刷新
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.User_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        UserVideoAdapter adapter = new UserVideoAdapter(fruitList);
        recyclerView.setAdapter(adapter);

    }




}