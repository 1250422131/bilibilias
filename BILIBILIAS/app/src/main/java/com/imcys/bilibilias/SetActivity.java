package com.imcys.bilibilias;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;

import java.io.IOException;

public class SetActivity extends AppCompatActivity {

    private String cookie;
    private String csrf;
    private String DLPath;
    private AppFilePathUtils mAppFilePathUtils ;
    private String DownloadMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        //显示缓存大小
        setCacheSize();
        //显示下载方式
        getDownloadMethod();
        try {
            String SPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/Path.txt");
            String DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/断点续传设置.txt");
            Switch Switch1 = (Switch)findViewById(R.id.Set_Switch);
            TextView SPath1TextView = (TextView)findViewById(R.id.Set_SPath);
            if (DLPath.equals("1")){
                Switch1.setChecked(true);
            }else{
                Switch1.setChecked(false);
            }
            SPath1TextView.setText(SPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //下载方式获取
    private void getDownloadMethod(){
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        DownloadMethod = sharedPreferences.getString("DownloadMethod", "");
        TextView DownloadMethodText = findViewById(R.id.Set_Download_Method);
        if(DownloadMethod.equals("")){
            DownloadMethodText.setText("未知方案");
        }else{
            if(DownloadMethod.equals("1")){
                DownloadMethodText.setText("弹窗下载");
            }else {
                DownloadMethodText.setText("后台下载");
            }
        }
    }


    //点击事件

    //设置下载方式
    public void setDownloadMethod(View view){
        AlertDialog aldg;
        AlertDialog.Builder adBd=new AlertDialog.Builder(SetActivity.this);
        adBd.setTitle("设置介绍");
        String ht = "\n\n后台模式：这是直接调用系统下载管理器，是我们目前的一个新方法。但为了适配安卓7及以上，不能更改储存位置。默认储存位置/storage/emulated/0/Download/com.imcys.bilibilias/";
        adBd.setMessage("亲爱的用户，在我不断尝试下，终于解决了下载方式问题，支持了后台下载，现在请看我为您介绍的下载方式。\n\n弹窗模式：这是一种传统的下载方式，可以灵活的改变下载位置，缺点是需要一直在软件界面等待。" + ht);
        adBd.setPositiveButton("弹窗下载", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //步骤3：将获取过来的值放入文件
            editor.putString("DownloadMethod", "1");
            //步骤4：提交 commit有返回值apply没有
            editor.apply();
        });
        adBd.setNegativeButton("后台下载", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //步骤3：将获取过来的值放入文件
            editor.putString("DownloadMethod", "2");
            //步骤4：提交 commit有返回值apply没有
            editor.apply();
        });
        aldg=adBd.create();
        aldg.show();
    }

    public void SetDd(View view){
        Switch Switch1 = (Switch)findViewById(R.id.Set_Switch);
        try {
            String DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/断点续传设置.txt");
            String SetPath = getExternalFilesDir("下载设置").toString()+"/断点续传设置.txt";
            if (DLPath.equals("1")){
                BilibiliPost.fileWrite(SetPath,"0");
            }else{
                BilibiliPost.fileWrite(SetPath,"1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetDLPath(View view){
        FileChooser fileChooser = new FileChooser(SetActivity.this, new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                ((TextView) findViewById(R.id.Set_SPath)).setText(filePath+"/");
                try
                {
                    String DLPath = getExternalFilesDir("下载设置").toString()+"/Path.txt";
                    BilibiliPost.fileWrite(DLPath,filePath+"/");
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        });
        fileChooser.setTitle("选择储存目录");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorAccent);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_FOLDER);
        fileChooser.showFile(false);  //是否显示文件
        fileChooser.open();
    }


    public void goClearCache(View view){
        mAppFilePathUtils = new AppFilePathUtils(SetActivity.this,"com.imcys.bilibilias");
        AlertDialog aldg;
        AlertDialog.Builder adBd=new AlertDialog.Builder(SetActivity.this);
        adBd.setTitle("清理提示");
        adBd.setMessage("嘻嘻，这个是运行软件过程产生的垃圾文件，不是下载的视频哦，放心清理吧！");
        adBd.setPositiveButton("清理缓存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppFilePathUtils.deleteDir(mAppFilePathUtils.getCache());
                Toast.makeText(SetActivity.this,"清理完成",Toast.LENGTH_SHORT).show();
                setCacheSize();
            }
        });
        adBd.setNegativeButton("我内存大", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        aldg=adBd.create();
        aldg.show();
    }

    private void setCacheSize(){
        mAppFilePathUtils = new AppFilePathUtils(SetActivity.this,"com.imcys.bilibilias");
        TextView mCachTextView = findViewById(R.id.Set_CacheTextView);
        final long total = AppFilePathUtils.getTotalSizeOfFilesInDir(mAppFilePathUtils.getCache());
        double cacheNc = (double) (total / 1048576);
        new Thread(()->{
            runOnUiThread(()->{
                mCachTextView.setText(cacheNc+"MB");
            });
        }).start();
    }

    public void GoBz(View view){
        Uri uri = Uri.parse("https://space.bilibili.com/351201307/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void GoGithub(View view){
        Uri uri = Uri.parse("https://github.com/1250422131/bilibilias");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void UserExit(View view){
        AlertDialog aldg;
        AlertDialog.Builder adBd=new AlertDialog.Builder(SetActivity.this);
        adBd.setTitle("警告");
        adBd.setMessage("awa，那个那个，要离开本程序同时清除B站服务器的登录身份记录吗？");
        adBd.setPositiveButton("嗯嗯，郑要走惹", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String csrfPath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"csrf.txt";
                        String CookiePath = getExternalFilesDir("哔哩哔哩视频").toString()+"/"+"cookie.txt";
                        try {
                            csrf = BilibiliPost.fileRead(csrfPath);
                            cookie = BilibiliPost.fileRead(CookiePath);
                            HttpUtils.doPost("https://passport.bilibili.com/login/exit/v2","biliCSRF="+csrf,cookie);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "退出完成", Toast.LENGTH_SHORT).show();
                                System.exit(0);
                            }
                        });
                    }

                }).start();
            }
        });
        adBd.setNegativeButton("手滑惹", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        aldg=adBd.create();
        aldg.show();
    }




}