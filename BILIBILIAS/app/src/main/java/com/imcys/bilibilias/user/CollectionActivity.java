package com.imcys.bilibilias.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.baidu.mobstat.StatService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.imcys.bilibilias.home.VerificationUtils;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {


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
    private TabLayout tabLayout;
    private ArrayList<String> collectionNameList;
    private ArrayList<Object> collectionIdList;
    private ArrayList<Object> mediaCountList;
    private int tabLayNumber;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PullToRefreshView mPullToRefreshView;
    private String soText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        //加载标题
        newTool();
        //获取用户的个人信息
        intenNew();
        //加载视频列表
        init();
        CollectionList();

    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    private void init() {


        tabLayout = (TabLayout) findViewById(R.id.Collection_TabLayout);
        tabLayNumber = 0;
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.Collection_SwipeRefreshLayout);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (SRL < (int) media_count) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SRL = SRL + 1;
                                    String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id=" + collectionId + "&pn=" + SRL + "&ps=20&keyword=&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp", cookie);
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
                                                    UserVideoList.add(0, VideoListData);
                                                    adapter.notifyDataSetChanged();

                                                }
                                                //执行刷新
                                                mPullToRefreshView.setRefreshing(false);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }).start();
                        } else {
                            mPullToRefreshView.setRefreshing(false);
                            Snackbar.make(findViewById(R.id.Total_ConstraintLayout), "o(´^｀)o这次真到底啦", Snackbar.LENGTH_SHORT).show();
                        }
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 5);
            }
        });

    }

    private void intenNew() {
        intent = getIntent();
        mid = intent.getStringExtra("mid");
        cookie = intent.getStringExtra("cookie");

    }

    private void newTool() {
        collectionNameList = new ArrayList<>();
        collectionIdList = new ArrayList<>();
        mediaCountList = new ArrayList<>();
        androidx.appcompat.widget.Toolbar mToolbar = (Toolbar) findViewById(R.id.Collection_Total_Toolbar);
        mToolbar.setTitle("收藏视频");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.as_menu, menu);
        //Toolbar的搜索框
        MenuItem searchItem = menu.findItem(R.id.as_toolbar_search);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        SearchView finalSearchView = searchView;
        searchView.setIconified(true);
        //searchView 的 textView 控件绑定

        TextView textView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        searchView.setQueryHint("输入查询关键字");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //折合SearchView
                if (query.length() > 0) {
                    //折合SearchView
                    finalSearchView.setIconified(true);
                }
                pd2 = ProgressDialog.show(CollectionActivity.this, "提示", "正在拉取数据");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        soText = query;
                        String videoData = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id=" + collectionId + "&pn=1&ps=20&keyword=" + soText + "&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp", cookie);
                        try {
                            JSONObject videoJson = new JSONObject(videoData);
                            int pd = videoJson.getInt("code");

                            //判断接口弃坑
                            if (pd == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject CollectionJson = new JSONObject(videoData);
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
                                                UserVideoList.add(0, VideoListData);
                                                adapter.notifyDataSetChanged();
                                                //执行刷新
                                                mPullToRefreshView.setRefreshing(false);
                                            }
                                            pd2.cancel();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                soText = s;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String videoData = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id=" + collectionId + "&pn=1&ps=20&keyword=" + soText + "&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp", cookie);
                        try {
                            JSONObject videoJson = new JSONObject(videoData);
                            int pd = videoJson.getInt("code");

                            //判断接口弃坑
                            if (pd == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject CollectionJson = new JSONObject(videoData);
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
                                                UserVideoList.add(0, VideoListData);
                                                adapter.notifyDataSetChanged();
                                                //执行刷新
                                                mPullToRefreshView.setRefreshing(false);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //设置Menu点击事件
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;

    }

    private void CollectionList() {
        pd2 = ProgressDialog.show(CollectionActivity.this, "提示", "正在拉取用户数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserCollectionJson = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/folder/created/list?pn=1&ps=10&up_mid=" + mid + "&jsonp=jsonp", cookie);
                try {
                    JSONObject UserCollectionData = new JSONObject(UserCollectionJson);
                    UserCollectionData = UserCollectionData.getJSONObject("data");
                    JSONArray UserCollectionArray = UserCollectionData.getJSONArray("list");
                    for (int i = 0; i < UserCollectionArray.length(); i++) {
                        UserCollectionData = UserCollectionArray.getJSONObject(i);
                        media_count = UserCollectionData.getDouble("media_count");
                        String title = UserCollectionData.getString("title");
                        int id = UserCollectionData.getInt("id");
                        collectionNameList.add(title);
                        collectionIdList.add(id);
                        mediaCountList.add(media_count);
                    }
                    media_count = (double) mediaCountList.get(tabLayNumber);
                    media_count = Math.ceil(media_count / 20);
                    collectionId = (int) collectionIdList.get(tabLayNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id=" + collectionId + "&pn=1&ps=20&keyword=&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp", cookie);
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
                                try {
                                    play = VerificationUtils.DigitalConversion(Integer.parseInt(play));
                                    Dm = VerificationUtils.DigitalConversion(Integer.parseInt(Dm));
                                    UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, CollectionActivity.this);
                                    UserVideoList.add(VideoListData);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (int i = 0; i < collectionNameList.size(); i++) {
                                tabLayout.addTab(tabLayout.newTab().setText(collectionNameList.get(i)));
                            }
                            //执行刷新
                            //绑定tab点击事件
                            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    tabLayNumber = tab.getPosition();
                                    TabLayoutTabOn();
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });
                            recyclerView = (RecyclerView) findViewById(R.id.Collection_Video_Ranking);
                            GridLayoutManager layoutManager = new GridLayoutManager(CollectionActivity.this, 2);
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
                                        Snackbar.make(findViewById(R.id.Collection_Total_Toolbar), "呜哇，我是有底线的", Snackbar.LENGTH_LONG)
                                                .setAction("加载下一页", v -> SlideToBottom()).show();
                                    }
                                }
                            });
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


    private void TabLayoutTabOn() {
        UserVideoList.clear();
        pd2 = ProgressDialog.show(CollectionActivity.this, "提示", "正在拉取用户数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                media_count = (double) mediaCountList.get(tabLayNumber);
                media_count = Math.ceil(media_count / 20);
                collectionId = (int) collectionIdList.get(tabLayNumber);
                String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id=" + collectionId + "&pn=1&ps=20&keyword=" + soText + "&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp", cookie);
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
                                try {
                                    play = VerificationUtils.DigitalConversion(Integer.parseInt(play));
                                    Dm = VerificationUtils.DigitalConversion(Integer.parseInt(Dm));
                                    UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, CollectionActivity.this);
                                    UserVideoList.add(VideoListData);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();
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


    private void SlideToBottom() {
        if (SRL < (int) media_count) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SRL = SRL + 1;
                    String CollectionStr = HttpUtils.doGet("https://api.bilibili.com/x/v3/fav/resource/list?media_id=" + collectionId + "&pn=" + SRL + "&ps=20&keyword=" + soText + "&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp", cookie);
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
                                    try {
                                        play = VerificationUtils.DigitalConversion(Integer.parseInt(play));
                                        Dm = VerificationUtils.DigitalConversion(Integer.parseInt(Dm));
                                        UserVideo VideoListData = new UserVideo(Title, bvid, aid, pic, play, Dm, CollectionActivity.this);
                                        UserVideoList.add(VideoListData);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    adapter.notifyDataSetChanged();
                                    //执行刷新
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
