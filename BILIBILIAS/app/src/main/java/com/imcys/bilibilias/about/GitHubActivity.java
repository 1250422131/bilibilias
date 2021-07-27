package com.imcys.bilibilias.about;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.imcys.bilibilias.R;

import java.util.ArrayList;
import java.util.List;


public class GitHubActivity extends AppCompatActivity {


    private List<Support> SupportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SupportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ranking);
        newTool();

        String[] NameList = {"DialogX", "BaiDuMobStat", "YouthBanner", "GoogleExoplayer", "JiaoZiVideoPlayer", "supertaohaili", "CtiaoDFM", "Jsoup", "Bumptech"};
        String[] ModeList = {"对话框", "百度移动统计", "轮播图", "谷歌的某个播放器", "饺子播放器", "文件选择器", "烈焰弹幕使", "网络请求", "图片处理"};

        for (int i = 0; i < NameList.length; i++) {
            Support SupportListData = new Support(NameList[i], ModeList[i], "https://i.loli.net/2021/04/10/78F5aYImBp3qRje.png", GitHubActivity.this);
            SupportList.add(SupportListData);
        }
        //执行刷新
        recyclerView = (RecyclerView) findViewById(R.id.As_Video_Ranking);
        GridLayoutManager layoutManager = new GridLayoutManager(GitHubActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SupportAdapter(SupportList);
        recyclerView.setAdapter(adapter);
    }

    private void newTool() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.Total_Toolbar);
        mToolbar.setTitle("开源使用");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //设置Menu点击事件
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.home:
                this.finish();
                break;
        }
        return true;

    }

}
