package com.imcys.bilibilias;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.imcys.bilibilias.as.VideoAsActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SetActivity extends AppCompatActivity {

    private String cookie;
    private String csrf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        //隐藏自带标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.hide();
        }
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
    //点击事件
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
        final EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请注意开头\"/\"已经给出\n请记得加路径末尾的\"/\"");
        builder.setView(et);
        et.setHint("无需输入 /storage/emulated/0/");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SetActivity.this, "/storage/emulated/0/"+et.getText(), Toast.LENGTH_SHORT).show();
                try
                {
                    String DLPath = getExternalFilesDir("下载设置").toString()+"/Path.txt";
                    BilibiliPost.fileWrite(DLPath,"/storage/emulated/0/"+et.getText().toString());
                    Field field = dialog.getClass().getSuperclass().getDeclaredField( "mShowing" );
                    field.setAccessible( true );
                    field.set( dialog, false); // false - 使之不能关闭
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        });
        builder.show();
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