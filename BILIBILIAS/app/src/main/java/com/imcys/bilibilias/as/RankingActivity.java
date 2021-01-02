package com.imcys.bilibilias.as;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;

import com.imcys.bilibilias.user.UserVideo;
import com.imcys.bilibilias.user.UserVideoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private String cookie;
    private String csrf;
    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ranking);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("热榜视频");


        pd2 = ProgressDialog.show(RankingActivity.this, "提示", "正在拉取用户数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                try {
                    csrf = BilibiliPost.fileRead(csrfPath);
                    cookie = BilibiliPost.fileRead(CookiePath);
                    String rankingData = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all",cookie);
                    JSONObject rankingJson = new JSONObject(rankingData);
                    rankingJson = rankingJson.getJSONObject("data");
                    JSONArray rankingArray = rankingJson.getJSONArray("list");

                    for (int i = 0; i<rankingArray.length();i++){
                        JSONObject rankingJsonData = rankingArray.getJSONObject(i);
                        JSONObject videoJson = rankingJsonData.getJSONObject("stat");
                        String Title = rankingJsonData.getString("title");
                        String pic = rankingJsonData.getString("pic");
                        String play = videoJson.getString("view");
                        String Dm = videoJson.getString("danmaku");
                        String bvid = rankingJsonData.getString("bvid");
                        String aid = rankingJsonData.getString("aid");
                        UserVideo VideoListData = new UserVideo(Title,bvid,aid,pic,play,Dm, RankingActivity.this);
                        fruitList.add(VideoListData);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //执行刷新
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(RankingActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        UserVideoAdapter adapter = new UserVideoAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        pd2.cancel();
                    }
                });
            }

        }).start();
    }

}
