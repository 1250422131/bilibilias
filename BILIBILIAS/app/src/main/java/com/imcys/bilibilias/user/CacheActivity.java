package com.imcys.bilibilias.user;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.tabs.TabLayout;
import com.imcys.bilibilias.AppFilePathUtils;

import com.imcys.bilibilias.BilibiliPost;

import com.imcys.bilibilias.R;

import com.imcys.bilibilias.fileUriUtils;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CacheActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private List<Cache> CacheList = new ArrayList<>();
    private ListPreference mDLPreference;
    private TabLayout tabLayout;
    private int tabLayNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cache);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CacheActivity.this);
        newTool();
        init();
        VideoLoad();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


    }

    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.Collection_TabLayout);
        tabLayNumber = 0;
        tabLayout.addTab(tabLayout.newTab().setText("视频管理"));
        tabLayout.addTab(tabLayout.newTab().setText("弹幕管理"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayNumber = tab.getPosition();
                if (tabLayNumber == 1) {
                    DMLoad();
                } else {
                    VideoLoad();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    //Tool标题设置与返回按键
    private void newTool() {
        androidx.appcompat.widget.Toolbar mToolbar = (Toolbar) findViewById(R.id.User_Cache_Toolbar);
        mToolbar.setTitle("缓存管理");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //视频文件加载
    private void VideoLoad() {
        CacheList.clear();
        ArrayList<File> mFileName = AppFilePathUtils.getAllDataFileName(sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)));
        for (int i = 0; i < mFileName.size(); i++) {
            String FileData = mFileName.get(i).getName();
            int lengthString = FileData.length();
            if (FileData.startsWith("mp4", lengthString - 3) || FileData.startsWith("flv", lengthString - 3)) {
                if (FileData.substring(lengthString - 4, lengthString).contains(".")) {
                    double fileSize = (double) (mFileName.get(i).length() / 1048576);
                    Cache CacheListData = new Cache(FileData.substring(0, lengthString - 4), FileData.substring(lengthString - 3, lengthString), fileSize + "MB", sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)) + FileData, CacheActivity.this);
                    CacheList.add(CacheListData);
                }
            }
        }
        //执行刷新
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.User_Cache_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CacheActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        CacheAdapter adapter = new CacheAdapter(CacheList);

        adapter.setOnItemClickListener(new CacheAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Cache cache = CacheList.get(position);

                BottomMenu.show(new String[]{"分享文件", "删除文件"})
                        .setMessage("选择操作")
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                switch (text.toString()) {
                                    case "分享文件":
                                        share(CacheActivity.this,"分享文件", FileProvider.getUriForFile(CacheActivity.this,"com.imcys.bilibilias.fileProvider",new File(cache.getPath())),"video/*");

                                        break;
                                    case "删除文件":
                                        MessageDialog.build()
                                                .setTitle("删除提示")
                                                .setMessage("要删除这个文件吗？")
                                                //设置按钮文本并设置回调
                                                .setOkButton("嗯嗯", new OnDialogButtonClickListener<MessageDialog>() {
                                                    @Override
                                                    public boolean onClick(MessageDialog baseDialog, View v) {
                                                        BilibiliPost.deleteFile(cache.getPath());
                                                        CacheList.remove(position);
                                                        adapter.notifyDataSetChanged();
                                                        return false;
                                                    }
                                                })
                                                .setCancelButton("手滑惹", null).show();
                                        break;
                                }
                                return false;
                            }
                        });


            }

        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void DMLoad() {
        CacheList.clear();
        ArrayList<File> mFileName = AppFilePathUtils.getAllDataFileName(sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)));
        for (int i = 0; i < mFileName.size(); i++) {
            String FileData = mFileName.get(i).getName();
            int lengthString = FileData.length();
            if (FileData.startsWith("xml", lengthString - 3)) {
                if (FileData.substring(lengthString - 4, lengthString).contains(".")) {
                    double fileSize = (double) (mFileName.get(i).length() / 1048576);
                    Cache CacheListData = new Cache(FileData.substring(0, lengthString - 4), FileData.substring(lengthString - 3, lengthString), fileSize + "MB", sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)) + FileData, CacheActivity.this);
                    CacheList.add(CacheListData);
                }
            }
        }
        /**
         if (FileData.substring(lengthString - 4, lengthString).contains(".")) {
         Cache CacheListData = new Cache(FileData.substring(0, lengthString - 4), FileData.substring(lengthString - 3, lengthString), sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)) + FileData, CacheActivity.this);
         CacheList.add(CacheListData);
         } else if (FileData.substring(lengthString - 5, lengthString).contains(".")) {
         Cache CacheListData = new Cache(FileData.substring(0, lengthString - 5), FileData.substring(lengthString - 4, lengthString), sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)) + FileData, CacheActivity.this);
         CacheList.add(CacheListData);
         }
         */

        //执行刷新
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.User_Cache_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CacheActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        CacheAdapter adapter = new CacheAdapter(CacheList);
        adapter.setOnItemClickListener(new CacheAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Cache cache = CacheList.get(position);
                BottomMenu.show(new String[]{"分享文件", "删除文件"})
                        .setMessage("选择操作")
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                switch (text.toString()) {
                                    case "分享文件":
                                        share(CacheActivity.this,"分享文件", FileProvider.getUriForFile(CacheActivity.this,"com.imcys.bilibilias.fileProvider",new File(cache.getPath())),"text/*");
                                        break;
                                    case "删除文件":
                                        MessageDialog.build()
                                                .setTitle("删除提示")
                                                .setMessage("要删除这个文件吗？")
                                                //设置按钮文本并设置回调
                                                .setOkButton("嗯嗯", new OnDialogButtonClickListener<MessageDialog>() {
                                                    @Override
                                                    public boolean onClick(MessageDialog baseDialog, View v) {
                                                        BilibiliPost.deleteFile(cache.getPath());
                                                        CacheList.remove(position);
                                                        adapter.notifyDataSetChanged();
                                                        return false;
                                                    }
                                                })
                                                .setCancelButton("手滑惹", null).show();
                                        break;
                                }
                                return false;
                            }
                        });
            }

        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void shareFile(String filePath, String fileType) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(fileType);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享文件"));
    }



    public static void share(Context context, String content, Uri uri ,String type) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType(type);
            //当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", content);
        } else {
            shareIntent.setType(type);
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        //自定义选择框的标题
        context.startActivity(Intent.createChooser(shareIntent, "分享文件"));
        //系统默认标题
    }



}

