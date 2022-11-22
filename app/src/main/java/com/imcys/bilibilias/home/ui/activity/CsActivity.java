package com.imcys.bilibilias.home.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.base.BaseActivity;
import com.imcys.bilibilias.home.ui.model.PanBean;
import com.imcys.bilibilias.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author:imcys
 * @create: 2022-11-22 19:05
 * @Description:
 */
public class CsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPan();

    }

    public void getPan() {
        HttpUtils.get("https://www.123pan.com/s/fSQ0Vv-4bm13", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //解析
                String str = response.body().string();
                String pattern = "window.g_initialProps = (.*?);";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(str);
                m.find();
                //转换为json
                toJson(m.group(1));
            }
        });
    }

    private void toJson(String group) {
        PanBean panBean = new Gson().fromJson(group, PanBean.class);
        HashMap<String, String> postJsonMap = new HashMap();

        postJsonMap.put("Etag", panBean.getReslist().getData().getInfoList().get(0).getEtag());
        postJsonMap.put("FileID", panBean.getReslist().getData().getInfoList().get(0).getFileId() + "");
        postJsonMap.put("S3keyFlag", panBean.getReslist().getData().getInfoList().get(0).getS3KeyFlag());
        postJsonMap.put("ShareKey", panBean.getRes().getData().getShareKey());
        postJsonMap.put("Size", panBean.getReslist().getData().getInfoList().get(0).getSize() + "");

        HttpUtils.postJson("https://www.123pan.com/a/api/share/download/info",

                new Gson().toJson(postJsonMap),
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d("测试", response.body().string());
                    }
                }
        );


    }
}
