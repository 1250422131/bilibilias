package com.imcys.bilibilias.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;


import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.Widget.Adapter.LikeAnimationAdapter;

import com.imcys.bilibilias.Widget.Adapter.MyRemoteViewsFactory;
import com.imcys.bilibilias.Widget.Adapter.MyRemoteViewsService;
import com.imcys.bilibilias.Widget.Service.LikeAnimationService;
import com.imcys.bilibilias.home.BiliLoginActivity;
import com.imcys.bilibilias.home.LoginActivity;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.home.RegUserActivity;
import com.imcys.bilibilias.home.RetrieveActivity;
import com.imcys.bilibilias.user.UserVideo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class LikeAnimationWidget extends AppWidgetProvider {
    private String cookie;
    private int mid;
    private RemoteViews rv;
    private ComponentName cn;
    private AppWidgetManager manager;
    private LikeAnimationAdapter mLikeAnimationAdapter;
    private RecyclerView recyclerView;
    private LayoutInflater mLayoutInflater;
    private String clickAction = "itemClick";
    private final String ACTION_UPDATE_ALL = "com.imcys.bilibilias.widget.UPDATE_ALL";

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.i("AppWidget", "收到点击");
        if (action.equals("refresh")) {
            // 刷新Widget
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, LikeAnimationWidget.class);
            // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.Widget_ListView);

        } else if (action.equals(clickAction)) {
            //intent.setClass(context, NewHomeActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent);
            try {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bilibili.com/video/"+intent.getStringExtra("content")));
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PackageManager packageManager = context.getPackageManager();
                ComponentName componentName = intent1.resolveActivity(packageManager);
                if (componentName != null) {
                    context.startActivity(intent1);
                } else {
                    Toast.makeText(context.getApplicationContext(), "呜哇，好像没有安装B站", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // 单击Wdiget中ListView的某一项会显示一个Toast提示。
            Toast.makeText(context, intent.getStringExtra("content"), Toast.LENGTH_SHORT).show();


        } else if (action.equals(ACTION_UPDATE_ALL)) {

            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, LikeAnimationWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.Widget_ListView);
        }

    }


    public String getCookie() {
        String cookie = "";
        String CookiePath = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/哔哩哔哩视频/cookie.txt";
        try {
            File file = new File(CookiePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            cookie = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i("AppWidget", "开始了更新");


        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserCollectionJson = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/history/cursor?max=0&view_at=0&business=", getCookie());
                try {
                    JSONObject UserCollectionData = new JSONObject(UserCollectionJson);
                    UserCollectionData = UserCollectionData.getJSONObject("data");
                    JSONArray UserCollectionArray = UserCollectionData.getJSONArray("list");
                    UserCollectionData = UserCollectionData.getJSONObject("cursor");
                    try {
                        MyRemoteViewsFactory.UserVideoList.clear();
                        for (int i = 0; i < UserCollectionArray.length(); i++) {
                            JSONObject UserVideoJson = UserCollectionArray.getJSONObject(i);
                            JSONObject UserVideoCntInfo = UserVideoJson.getJSONObject("history");
                            String Title = UserVideoJson.getString("title");
                            String pic = UserVideoJson.getString("cover");
                            String play = "未知";
                            String Dm = "未知";
                            String bvid = UserVideoCntInfo.getString("bvid");
                            String aid = UserVideoCntInfo.getString("oid");
                            MyRemoteViewsFactory.UserVideoList.add(new UserVideo(Title, bvid, aid, pic, play, Dm, context));

                        }


                        // 获取Widget的组件名
                        ComponentName thisWidget = new ComponentName(context,
                                LikeAnimationWidget.class);

                        // 创建一个RemoteView
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                R.layout.app_widget_like_animation);

                        // 把这个Widget绑定到RemoteViewsService
                        Intent intent = new Intent(context, MyRemoteViewsService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

                        // 设置适配器
                        remoteViews.setRemoteAdapter(R.id.Widget_ListView, intent);

                        // 设置当显示的widget_list为空显示的View
                        remoteViews.setEmptyView(R.id.Widget_ListView, R.layout.app_widget_like_animation);

                        // 点击列表触发事件
                        Intent clickIntent = new Intent(context, LikeAnimationWidget.class);
                        // 设置Action，方便在onReceive中区别点击事件
                        clickIntent.setAction(clickAction);
                        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

                        PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        remoteViews.setPendingIntentTemplate(R.id.Widget_ListView, pendingIntentTemplate);

                        // 刷新按钮
                        final Intent refreshIntent = new Intent(context, LikeAnimationWidget.class);
                        refreshIntent.setAction("refresh");
                        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        remoteViews.setOnClickPendingIntent(R.id.button_refresh, refreshPendingIntent);

                        //rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_user);
                        //这里获得当前的包名，并且用ApsetOnClickPendingIntentpWidgetManager来向NewAppWidget.class发送广播。
                        //manager = AppWidgetManager.getInstance(context);
                        //cn = new ComponentName(context, UserWidget.class);

                        // 更新Wdiget
                        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //context.stopService(new Intent(context, WidgetService.class));
        Log.i("AppWidget", "删除成功！");
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Intent mTimerIntent = new Intent(context, WidgetService.class);
        // context.startService(mTimerIntent);
        rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_like_animation);
        //这里获得当前的包名，并且用AppWidgetManager来向NewAppWidget.class发送广播。
        manager = AppWidgetManager.getInstance(context);
        cn = new ComponentName(context, LikeAnimationWidget.class);

        Log.i("AppWidget", "创建成功！");
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //  Intent mTimerIntent = new Intent(context, WidgetService.class);
        // context.stopService(mTimerIntent);
        Log.i("AppWidget", "删除成功！");

    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }


}

