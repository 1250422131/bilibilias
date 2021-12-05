package com.imcys.bilibilias.user;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.baidu.mobstat.StatService;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.SetActivity;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.home.VersionActivity;


public class AboutActivity extends AppCompatActivity {

    private String cookie;
    private String csrf;
    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
    private static String fileName = "/data/data/com.imcys.bilibilias/cache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


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
        Uri uri = Uri.parse("https://api.misakamoe.com/app/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void Privacy(View view){
        LinearLayout lLayout = new LinearLayout(this);
        lLayout.setOrientation(LinearLayout.VERTICAL);
        WebView Privacy = new WebView(AboutActivity.this);
        Privacy.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        Privacy.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        Privacy.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        Privacy.getSettings().setDomStorageEnabled(true);//DOM Storage 重点是设置这个
        Privacy.getSettings().setAllowFileAccess(false);
        Privacy.loadUrl("https://docs.qq.com/doc/DVWdlb2hSWFlJaUFk");
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1500);//这个属性是设置空间的长宽，其实还可以设置其他的控件的其他属性；
        lLayout.addView(Privacy, lParams);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AboutActivity.this);
        builder.setView(lLayout);
        builder.setCancelable(false);
        builder.setTitle("隐私政策");
        builder.setPositiveButton("使用程序代表同意协议", null);
        builder.show();

    }

    public void goQQQun(View view){
        joinQQGroup("g9z43_X-5QfTsFtZcP4-VCIQ8i-Sbjgw");
    }

    public void goWj(View view){
        Uri uri = Uri.parse("https://wj.qq.com/s2/7144284/6179");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void showNotifictionIcon(View view) {
        String id = "my_channel_01";
        String name="我是渠道名字";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setContentTitle("有新解析情况")
                    .setContentText("点击查看AV10086最新内容")
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("有新解析情况")
                    .setContentText("点击查看AV10086最新内容")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(111123, notification);
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