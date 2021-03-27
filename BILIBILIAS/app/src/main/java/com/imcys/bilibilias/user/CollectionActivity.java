package com.imcys.bilibilias.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity  extends AppCompatActivity  {


    private String mid;
    private String cookie;
    private String csrf;
    private List<UserVideo> UserVideoList = new ArrayList<>();
    private ProgressDialog pd2;
    private Intent intent;
    private int collectionId;
    private double media_count;
    private int SRL = 1;
    private RecyclerView recyclerView;
    private UserVideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ranking);

        //加载标题
        newTool();
        //获取用户的个人信息
        intenNew();
        //加载视频列表
        CollectionList();
    }

    private void intenNew(){
        intent = getIntent();
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");

    }

    private void newTool(){
        androidx.appcompat.widget.Toolbar mToolbar = (Toolbar) findViewById(R.id.Total_Toolbar);
        mToolbar.setTitle("收藏视频");
    }

    private void CollectionList(){
        pd2 = ProgressDialog.show(CollectionActivity.this, "提示", "正在拉取用户数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserCollectionJson = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/folder/created/list?pn=1&ps=10&up_mid="+mid+"&jsonp=jsonp",cookie);
                try {
                    JSONObject UserCollectionData = new JSONObject(UserCollectionJson);
                    UserCollectionData = UserCollectionData.getJSONObject("data");
                    JSONArray UserCollectionArray = UserCollectionData.getJSONArray("list");
                    UserCollectionData = UserCollectionArray.getJSONObject(0);
                    media_count = UserCollectionData.getDouble("media_count");
                    media_count = Math.ceil(media_count / 20);
                    collectionId = UserCollectionData.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id="+collectionId+"&pn=1&ps=20&keyword=&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp",cookie);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject CollectionJson = new JSONObject(CollectionStr);
                            CollectionJson = CollectionJson.getJSONObject("data");
                            JSONArray CollectionArray = CollectionJson.getJSONArray("medias");
                            for (int i = 0; i < CollectionArray.length(); i++) {
                                JSONObject UserVideoJson = CollectionArray.getJSONObject(i);
                                JSONObject UserVideoCntInfo = UserVideoJson.getJSONObject("cnt_info");
                                String Title = UserVideoJson.getString("title");
                                String pic = UserVideoJson.getString("cover");
                                String play = UserVideoCntInfo.getString("play");
                                String Dm = UserVideoCntInfo.getString("danmaku");
                                String bvid = UserVideoJson.getString("bvid");
                                String aid = UserVideoJson.getString("id");
                                UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, CollectionActivity.this);
                                UserVideoList.add(VideoListData);
                                //执行刷新
                                recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
                                GridLayoutManager layoutManager = new GridLayoutManager(CollectionActivity.this, 2);
                                recyclerView.setLayoutManager(layoutManager);
                                adapter = new UserVideoAdapter(UserVideoList);
                                recyclerView.setAdapter(adapter);;
                                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }
                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (isSlideToBottom(recyclerView)) {
                                            Snackbar.make(findViewById(R.id.Total_ConstraintLayout),"呜哇，我是有底线的", Snackbar.LENGTH_LONG)
                                                    .setAction("加载下一页", v -> SlideToBottom()).show();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd2.cancel();
                    }
                });
            }
        }).start();
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


    private void SlideToBottom(){
        if(SRL<(int)media_count){
            new Thread(()->{
                runOnUiThread(()->{
                    UserVideoList.clear();
                    adapter.notifyDataSetChanged();
                });
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SRL = SRL + 1;
                    String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id="+collectionId+"&pn="+SRL+"&ps=20&keyword=&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp",cookie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject CollectionJson = new JSONObject(CollectionStr);
                                CollectionJson = CollectionJson.getJSONObject("data");
                                JSONArray CollectionArray = CollectionJson.getJSONArray("medias");
                                for (int i = 0; i < CollectionArray.length(); i++) {
                                    JSONObject UserVideoJson = CollectionArray.getJSONObject(i);
                                    JSONObject UserVideoCntInfo = UserVideoJson.getJSONObject("cnt_info");
                                    String Title = UserVideoJson.getString("title");
                                    String pic = UserVideoJson.getString("cover");
                                    String play = UserVideoCntInfo.getString("play");
                                    String Dm = UserVideoCntInfo.getString("danmaku");
                                    String bvid = UserVideoJson.getString("bvid");
                                    String aid = UserVideoJson.getString("id");
                                    UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, CollectionActivity.this);
                                    UserVideoList.add(VideoListData);
                                    //执行刷新
                                    recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
                                    GridLayoutManager layoutManager = new GridLayoutManager(CollectionActivity.this, 2);
                                    recyclerView.setLayoutManager(layoutManager);
                                    adapter = new UserVideoAdapter(UserVideoList);
                                    recyclerView.setAdapter(adapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        }else {
            Snackbar.make(findViewById(R.id.Total_ConstraintLayout), "o(´^｀)o这次真到底啦", Snackbar.LENGTH_SHORT).show();
        }
    }


}
