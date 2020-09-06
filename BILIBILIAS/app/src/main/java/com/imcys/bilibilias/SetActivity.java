package com.imcys.bilibilias;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        //隐藏自带标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.hide();
        }
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
}