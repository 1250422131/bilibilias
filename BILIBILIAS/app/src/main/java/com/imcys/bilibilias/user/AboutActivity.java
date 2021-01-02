package com.imcys.bilibilias.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.imcys.bilibilias.HomeActivity;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.SetActivity;


public class AboutActivity extends AppCompatActivity {

    private String cookie;
    private String csrf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("关于软件");
    }

    public void goSet(View view){
        Intent intent= new Intent();
        intent.setClass(AboutActivity.this, SetActivity.class);
        startActivity(intent);
    }

    public void goBiliBili(View view){
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setData(Uri.parse("https://space.bilibili.com/351201307"));
        intent.setAction("android.intent.action.VIEW");
        startActivity(intent);
    }

    public void goBiliBiliASUrl(View view){
        Uri uri = Uri.parse("https://api.misakaloli.com/app/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void goQQQun(View view){
        joinQQGroup("g9z43_X-5QfTsFtZcP4-VCIQ8i-Sbjgw");
    }

    public void goWj(View view){
        Uri uri = Uri.parse("https://wj.qq.com/s2/7144284/6179");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    /****************
     *
     * 发起添加群流程。群号：哔哩哔哩萌新交流群(703180724) 的 key 为： g9z43_X-5QfTsFtZcP4-VCIQ8i-Sbjgw
     * 调用 joinQQGroup(g9z43_X-5QfTsFtZcP4-VCIQ8i-Sbjgw) 即可发起手Q客户端申请加群 哔哩哔哩萌新交流群(703180724)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }




}