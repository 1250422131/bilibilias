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

public class PlaybackRecordActivity extends AppCompatActivity {


    private String mid;
    private String cookie;
    private String csrf;
    private List<UserVideo> UserVideoList = new ArrayList<>();
    private ProgressDialog pd2;
    private Intent intent;
    private double ps;
    private int max;
    private int view_at;
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

    private void intenNew() {
        intent = getIntent();
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");

    }

    private void newTool() {
        androidx.appcompat.widget.Toolbar mToolbar = (Toolbar) findViewById(R.id.Total_Toolbar);
        mToolbar.setTitle("播放历史");
    }

    private void CollectionList() {
        pd2 = ProgressDialog.show(PlaybackRecordActivity.this, "提示", "正在拉取用户数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserCollectionJson = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/history/cursor?max=0&view_at=0&business=", cookie);
                try {
                    JSONObject UserCollectionData = new JSONObject(UserCollectionJson);
                    UserCollectionData = UserCollectionData.getJSONObject("data");
                    JSONArray UserCollectionArray = UserCollectionData.getJSONArray("list");
                    UserCollectionData = UserCollectionData.getJSONObject("cursor");
                    ps = UserCollectionData.getDouble("ps");
                    view_at = UserCollectionData.getInt("view_at");
                    max = UserCollectionData.getInt("max");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < UserCollectionArray.length(); i++) {
                                    JSONObject UserVideoJson = UserCollectionArray.getJSONObject(i);
                                    JSONObject UserVideoCntInfo = UserVideoJson.getJSONObject("history");
                                    String Title = UserVideoJson.getString("title");
                                    String pic = UserVideoJson.getString("cover");
                                    String play = "未知";
                                    String Dm = "未知";
                                    String bvid = UserVideoCntInfo.getString("bvid");
                                    String aid = UserVideoCntInfo.getString("oid");
                                    UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, PlaybackRecordActivity.this);
                                    UserVideoList.add(VideoListData);
                                    //执行刷新
                                    recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
                                    GridLayoutManager layoutManager = new GridLayoutManager(PlaybackRecordActivity.this, 2);
                                    recyclerView.setLayoutManager(layoutManager);
                                    adapter = new UserVideoAdapter(UserVideoList);
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
                                                Snackbar.make(findViewById(R.id.Total_ConstraintLayout), "呜哇，我是有底线的", Snackbar.LENGTH_LONG)
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


    private void SlideToBottom() {
        if (SRL < (int) ps) {
            new Thread(() -> {
                runOnUiThread(() -> {
                    UserVideoList.clear();
                    adapter.notifyDataSetChanged();
                });
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SRL = SRL + 1;
                    String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/history/cursor?max=" + max + "&view_at=" + view_at + "&business=archive", cookie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject CollectionJson = new JSONObject(CollectionStr);
                                CollectionJson = CollectionJson.getJSONObject("data");
                                JSONObject UserCollectionData = CollectionJson.getJSONObject("cursor");
                                view_at = UserCollectionData.getInt("view_at");
                                max = UserCollectionData.getInt("max");
                                JSONArray CollectionArray = CollectionJson.getJSONArray("list");
                                for (int i = 0; i < CollectionArray.length(); i++) {
                                    JSONObject UserVideoJson = CollectionArray.getJSONObject(i);
                                    JSONObject UserVideoCntInfo = UserVideoJson.getJSONObject("history");
                                    String Title = UserVideoJson.getString("title");
                                    String pic = UserVideoJson.getString("cover");
                                    String play = "未知";
                                    String Dm = "未知";
                                    String bvid = UserVideoCntInfo.getString("bvid");
                                    String aid = UserVideoCntInfo.getString("oid");
                                    UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, PlaybackRecordActivity.this);
                                    UserVideoList.add(VideoListData);
                                    //执行刷新
                                    recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
                                    GridLayoutManager layoutManager = new GridLayoutManager(PlaybackRecordActivity.this, 2);
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
        } else {
            Snackbar.make(findViewById(R.id.Total_ConstraintLayout), "o(´^｀)o这次真到底啦", Snackbar.LENGTH_SHORT).show();
        }
    }


}

