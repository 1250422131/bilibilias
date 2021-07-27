package com.imcys.bilibilias.about;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.kongzue.dialogx.dialogs.WaitDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SupportActivity extends AppCompatActivity {


    private List<Support> SupportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SupportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ranking);
        newTool();
        String[] NameList = {"F@NGZH", "皇天一心", "灵教圣主"};
        String[] ModeList = {"QQ", "哔哩哔哩", "哔哩哔哩"};
        String[] UrlList = {"http://q1.qlogo.cn/g?b=qq&nk=531334426&s=100", "https://i1.hdslb.com/bfs/face/dc44b74fe07cd5e3e46705e4750abb0351c9812f.jpg", "https://i2.hdslb.com/bfs/face/09148f1afef94e9128e721c9e2fc82a43eea56bc.jpg"};
        for (int i = 0; i < NameList.length; i++) {
            Support SupportListData = new Support(NameList[i], ModeList[i], UrlList[i], SupportActivity.this);
            SupportList.add(SupportListData);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String charge = HttpUtils.doGet("https://api.bilibili.com/x/ugcpay-rank/elec/month/up?up_mid=351201307","");
                try {
                    JSONObject chargeJson = new JSONObject(charge);
                    chargeJson = chargeJson.getJSONObject("data");
                    JSONArray chargeArray = chargeJson.getJSONArray("list");
                    for (int i = 0; i < chargeArray.length(); i++) {
                        chargeJson = chargeArray.getJSONObject(i);
                        String Name = chargeJson.getString("uname");
                        String Mode = chargeJson.getString("message");
                        String Url = chargeJson.getString("avatar");
                        Support SupportListData = new Support(Name, Mode, Url, SupportActivity.this);
                        SupportList.add(SupportListData);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //执行刷新
                            recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
                            GridLayoutManager layoutManager = new GridLayoutManager(SupportActivity.this, 1);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new SupportAdapter(SupportList);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void newTool() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.Total_Toolbar);
        mToolbar.setTitle("开发支持");
    }
}
