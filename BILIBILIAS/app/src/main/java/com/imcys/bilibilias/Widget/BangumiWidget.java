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
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.Widget.Adapter.Bangumi;
import com.imcys.bilibilias.Widget.Adapter.BangumiRemoteViewsFactory;
import com.imcys.bilibilias.Widget.Adapter.BangumiRemoteViewsService;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.user.CacheActivity;
import com.imcys.bilibilias.user.UserActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;

public class BangumiWidget extends AppWidgetProvider {
    private String cookie;
    private int mid;
    public int pn;
    private RemoteViews rv;
    private ComponentName cn;
    private AppWidgetManager manager;
    private RecyclerView recyclerView;
    private String clickAction = "itemClick";

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.i("BILIBILIAS", "收到点击");
        if (action.equals("refresh")) {
            // 刷新Widget
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, BangumiWidget.class);
            // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.Widget_Bangumi_GridView);
        } else if (action.equals(clickAction)) {
            Log.i("BILIBILIAS", "处理点击");
            // 单击Wdiget中ListView的某一项会显示一个Toast提示。

            try {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bilibili.com/bangumi/play/ss"+intent.getStringExtra("content") + "/"));
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

            Toast.makeText(context, intent.getStringExtra("content"), Toast.LENGTH_SHORT).show();
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
        System.out.println("更新日期" + new Timestamp(System.currentTimeMillis()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/nav", getCookie());
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    UserDataJson = UserDataJson.getJSONObject("data");
                    mid = UserDataJson.getInt("mid");
                    int time = (int) (System.currentTimeMillis() / 100);
                    String UserBangumi = HttpUtils.doGet("https://api.bilibili.com/x/space/bangumi/follow/list?type=1&follow_status=0&pn=1&ps=15&vmid=" + mid + "&ts=" + time, getCookie());
                    JSONObject UserBangumiJson = new JSONObject(UserBangumi);
                    UserBangumiJson = UserBangumiJson.getJSONObject("data");
                    JSONArray BangumiArray = UserBangumiJson.getJSONArray("list");
                    BangumiRemoteViewsFactory.UserVideoList.clear();
                    for (int i = 0; i < BangumiArray.length(); i++) {
                        UserBangumiJson = BangumiArray.getJSONObject(i);
                        String badge = UserBangumiJson.getString("badge");
                        String cover = UserBangumiJson.getString("cover");
                        int season_id = UserBangumiJson.getInt("season_id");
                        String progress = UserBangumiJson.getString("progress");
                        JSONObject new_ep = UserBangumiJson.getJSONObject("new_ep");
                        String index_show = new_ep.getString("index_show");
                        String title = UserBangumiJson.getString("title");
                        BangumiRemoteViewsFactory.UserVideoList.add(new Bangumi(title, season_id, badge, index_show, progress, cover, context));

                    }

                    Log.i("BILIBILIAS", "余量" + BangumiRemoteViewsFactory.UserVideoList.size());

                    // 获取Widget的组件名
                    ComponentName thisWidget = new ComponentName(context, BangumiWidget.class);

                    // 创建一个RemoteView
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_bangumi_layout);

                    // 把这个Widget绑定到RemoteViewsService
                    Intent intent = new Intent(context, BangumiRemoteViewsService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

                    // 设置适配器
                    remoteViews.setRemoteAdapter(R.id.Widget_Bangumi_GridView, intent);

                    // 设置当显示的widget_list为空显示的View
                    remoteViews.setEmptyView(R.id.Widget_Bangumi_GridView, R.layout.widget_bangumi_layout);

                    // 点击列表触发事件
                    Intent clickIntent = new Intent(context, BangumiWidget.class);
                    // 设置Action，方便在onReceive中区别点击事件
                    clickIntent.setAction(clickAction);
                    clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

                    PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    remoteViews.setPendingIntentTemplate(R.id.Widget_Bangumi_GridView, pendingIntentTemplate);

                    // 刷新按钮
                    final Intent refreshIntent = new Intent(context, BangumiWidget.class);
                    refreshIntent.setAction("refresh");
                    final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.button_refresh, refreshPendingIntent);


                    //rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_user);
                    //这里获得当前的包名，并且用AppWidgetManager来向NewAppWidget.class发送广播。
                    //manager = AppWidgetManager.getInstance(context);
                    //cn = new ComponentName(context, UserWidget.class);

                    // 更新Wdiget
                    appWidgetManager.updateAppWidget(thisWidget, remoteViews);

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
        rv = new RemoteViews(context.getPackageName(), R.layout.widget_bangumi_layout);
        //这里获得当前的包名，并且用AppWidgetManager来向NewAppWidget.class发送广播。
        manager = AppWidgetManager.getInstance(context);
        cn = new ComponentName(context, BangumiWidget.class);

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

