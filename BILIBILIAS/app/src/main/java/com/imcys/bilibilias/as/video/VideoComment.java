package com.imcys.bilibilias.as.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.Reply;
import com.imcys.bilibilias.as.ReplyAdapter;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.imcys.bilibilias.user.UserVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoComment extends Fragment {

    private String bvId;
    private String Aid;
    private String Up;
    private String upName;
    private String Mid;
    private String Cookie;
    private Intent intent;
    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;
    private List<Reply> replyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.as_video_comment, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //获取首页用户信息变量
        Cookie = VideoAsActivity.cookie;
        bvId = VideoAsActivity.bvid;
        Aid = VideoAsActivity.aid;

        pd2 = ProgressDialog.show(getContext(), "提示", "正在加载评论");
        new Thread(new Runnable() {
            @Override
            public void run() {

                String replyJsonData = HttpUtils.doGet("https://api.bilibili.com/x/v2/reply/main?jsonp=jsonp&next=0&type=1&mode=3&oid=" + Aid, Cookie);
                try {
                    JSONObject replyJson = new JSONObject(replyJsonData);
                    int pd = replyJson.getInt("code");
                    if (pd == 0) {
                        JSONObject replyData = replyJson.getJSONObject("data");
                        JSONArray replyReplies = replyData.getJSONArray("replies");
                        replyList.clear();
                        for (int r = 0; r < replyReplies.length(); r++) {
                            JSONObject upUserJson = replyReplies.getJSONObject(r);
                            JSONObject upUserJsonStr = upUserJson.getJSONObject("member");
                            JSONObject upReplyJson = upUserJson.getJSONObject("content");
                            String msg = upReplyJson.getString("message");
                            String upName = upUserJsonStr.getString("uname");
                            String Url = upUserJsonStr.getString("avatar");
                            Reply ReplyListData = new Reply(upName, msg, Url, getActivity());
                            replyList.add(ReplyListData);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //执行刷新
                        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.As_Comment_RecyclerView);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        ReplyAdapter adapter = new ReplyAdapter(replyList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pd2.cancel();
                    }
                });
            }
        }).start();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}
