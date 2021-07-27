package com.imcys.bilibilias.as.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.imcys.bilibilias.home.VerificationUtils;

import com.imcys.bilibilias.user.UserVideo;
import com.imcys.bilibilias.user.UserVideoAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoRecommend extends Fragment {

    private String bvId;
    private String Aid;
    private String Up;
    private String upName;
    private String Mid;
    private String Cookie;
    private Intent intent;
    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.as_video_recommend, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //获取首页用户信息变量
        Cookie = VideoAsActivity.cookie;
        bvId = VideoAsActivity.bvid;

        //加载结束
        //视频列表加载事件
        pd2 = ProgressDialog.show(getActivity(), "提示", "正在拉取推荐视频");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserVideoStr = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/archive/related?bvid=" + bvId, Cookie);
                try {
                    JSONObject VideoJson = new JSONObject(UserVideoStr);
                    JSONArray VideoArray = VideoJson.getJSONArray("data");
                    fruitList.clear();
                    for (int i = 0; i < VideoArray.length(); i++) {
                        JSONObject UserVideoData = VideoArray.getJSONObject(i);
                        JSONObject UserVideoStat = UserVideoData.getJSONObject("stat");
                        String Title = UserVideoData.getString("title");
                        String pic = UserVideoData.getString("pic");
                        int play = UserVideoStat.getInt("view");
                        int Dm = UserVideoStat.getInt("danmaku");
                        String bvid = UserVideoData.getString("bvid");
                        String aid = UserVideoData.getString("aid");
                        UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, VerificationUtils.DigitalConversion(play), VerificationUtils.DigitalConversion(Dm) + "", getActivity());
                        fruitList.add(VideoListData);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //执行刷新
                            RecyclerView recyclerView = getActivity().findViewById(R.id.As_Recommend_RecyclerView);
                            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                            recyclerView.setLayoutManager(layoutManager);
                            UserVideoAdapter adapter = new UserVideoAdapter(fruitList);
                            recyclerView.setAdapter(adapter);
                            pd2.cancel();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}