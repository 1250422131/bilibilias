package com.imcys.bilibilias.Widget.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.Widget.BangumiWidget;
import com.imcys.bilibilias.user.CreativeCenterActivity;
import com.imcys.bilibilias.user.UserActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.widget.RemoteViewsService.RemoteViewsFactory;


public class BangumiRemoteViewsFactory implements RemoteViewsFactory {

    private final Context mContext;
    public static List<String> mList = new ArrayList<String>();
    public static ArrayList<Bangumi> UserVideoList = new ArrayList<Bangumi>();
    private int width;
    private int height;
    private int pn = 1;
    private int total;
    private int pageNum;

    /*
     * 构造函数
     */
    public BangumiRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    /*
     * MyRemoteViewsFactory调用时执行，这个方法执行时间超过20秒回报错。
     * 如果耗时长的任务应该在onDataSetChanged或者getViewAt中处理
     */
    @Override
    public void onCreate() {
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


    /*
     * 当调用notifyAppWidgetViewDataChanged方法时，触发这个方法
     * 例如：MyRemoteViewsFactory.notifyAppWidgetViewDataChanged();
     */
    @Override
    public void onDataSetChanged() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/nav", getCookie());
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    UserDataJson = UserDataJson.getJSONObject("data");
                    int mid = UserDataJson.getInt("mid");
                    int time = (int) (System.currentTimeMillis() / 100);
                    String UserBangumi = HttpUtils.doGet("https://api.bilibili.com/x/space/bangumi/follow/list?type=1&follow_status=0&pn="+pn+"&ps=15&vmid=" + mid + "&ts=" + time, getCookie());
                    JSONObject UserBangumiJson = new JSONObject(UserBangumi);
                    UserBangumiJson = UserBangumiJson.getJSONObject("data");
                    total = UserBangumiJson.getInt("total");
                    pageNum = (total % 15) == 0 ? (total / 15) : (total / 15) - 1;
                    JSONArray BangumiArray = UserBangumiJson.getJSONArray("list");
                    UserVideoList.clear();
                    for (int i = 0; i < BangumiArray.length(); i++) {
                        UserBangumiJson = BangumiArray.getJSONObject(i);
                        String badge = UserBangumiJson.getString("badge");
                        String cover = UserBangumiJson.getString("cover");
                        int season_id = UserBangumiJson.getInt("season_id");
                        String progress = UserBangumiJson.getString("progress");
                        JSONObject new_ep = UserBangumiJson.getJSONObject("new_ep");
                        String index_show = new_ep.getString("index_show");
                        String title = UserBangumiJson.getString("title");
                        UserVideoList.add(new Bangumi(title, season_id, badge, index_show, progress, cover, mContext));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void runWidget(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/nav", getCookie());
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    UserDataJson = UserDataJson.getJSONObject("data");
                    int mid = UserDataJson.getInt("mid");
                    int time = (int) (System.currentTimeMillis() / 100);
                    String UserBangumi = HttpUtils.doGet("https://api.bilibili.com/x/space/bangumi/follow/list?type=1&follow_status=0&pn="+pn+"&ps=15&vmid=" + mid + "&ts=" + time, getCookie());
                    JSONObject UserBangumiJson = new JSONObject(UserBangumi);
                    UserBangumiJson = UserBangumiJson.getJSONObject("data");
                    total = UserBangumiJson.getInt("total");


                    pageNum = (total % 15) == 0 ? (total / 15) : (total / 15) - 1;

                    JSONArray BangumiArray = UserBangumiJson.getJSONArray("list");
                    UserVideoList.clear();
                    for (int i = 0; i < BangumiArray.length(); i++) {
                        UserBangumiJson = BangumiArray.getJSONObject(i);
                        String badge = UserBangumiJson.getString("badge");
                        String cover = UserBangumiJson.getString("cover");
                        int season_id = UserBangumiJson.getInt("season_id");
                        String progress = UserBangumiJson.getString("progress");
                        JSONObject new_ep = UserBangumiJson.getJSONObject("new_ep");
                        String index_show = new_ep.getString("index_show");
                        String title = UserBangumiJson.getString("title");
                        UserVideoList.add(new Bangumi(title, season_id, badge, index_show, progress, cover, mContext));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /*
     * 这个方法不用多说了把，这里写清理资源，释放内存的操作
     */
    @Override
    public void onDestroy() {
        UserVideoList.clear();
    }

    /*
     * 返回集合数量
     */
    @Override
    public int getCount() {
        return UserVideoList.size();
    }

    /*
     * 创建并且填充，在指定索引位置显示的View，这个和BaseAdapter的getView类似
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Bangumi content = UserVideoList.get(position);
        // 创建在当前索引位置要显示的View
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_bangumi_widget);



        // 设置要显示的内容 ImageView WidgetImage = (ImageView) convertView.findViewById(R.id.Widget_Video_Image);
        //        TextView WidgetDm = (TextView) convertView.findViewById(R.id.Widget_Video_Dm);
        //        TextView WidgetPlay = (TextView) convertView.findViewById(R.id.Widget_Video_Play);
        //        TextView WidgetTitle = (TextView) convertView.findViewById(R.id.User_Video_Title);
        //        TextView WidgetAid = (TextView) convertView.findViewById(R.id.Widget_Video_Aid);
        //        TextView WidgetPic = (TextView) convertView.findViewById(R.id.Widget_Video_Pic);
        //        TextView WidgetBvid = (TextView) convertView.findViewById(R.id.Widget_Video_Bvid);
        Bitmap bitmap = getImageBitmap(content.getCover());
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        rv.setImageViewBitmap(R.id.Widget_Bangumi_Image, getRoundBitmapByShader(bitmap, width, height, 30, 0));
        rv.setTextViewText(R.id.Widget_Bangumi_Title, content.getTitle());
        rv.setTextViewText(R.id.Widget_Bangumi_Type, content.getBadge());
        rv.setTextViewText(R.id.Widget_Bangumi_Progress, content.getProgress());

        // 填充Intent，填充在AppWdigetProvider中创建的PendingIntent

        Intent intent = new Intent();
        // 传入点击行的数据
        intent.putExtra("content", content.getSeasonId()+"");
        rv.setOnClickFillInIntent(R.id.Widget_Bangumi_ly, intent);



        return rv;

    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
        }
        return bm;
    }


    /*
     * 显示一个"加载"View。返回null的时候将使用默认的View
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /*
     * 不同View定义的数量。默认为1（本人一直在使用默认值）
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /*
     * 返回当前索引的。
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * 如果每个项提供的ID是稳定的，即她们不会在运行时改变，就返回true（没用过。。。）
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 通过BitmapShader实现圆形边框
     *
     * @param bitmap
     * @param outWidth  输出的图片宽度
     * @param outHeight 输出的图片高度
     * @param radius    圆角大小
     * @param boarder   边框宽度
     */
    public static Bitmap getRoundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius, int boarder) {
        if (bitmap == null) {
            return null;
        }
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;


        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        //创建输出的bitmap
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //创建着色器
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //给着色器配置matrix
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        //创建矩形区域并且预留出border
        RectF rect = new RectF(boarder, boarder, outWidth - boarder, outHeight - boarder);
        //把传入的bitmap绘制到圆角矩形区域内
        canvas.drawRoundRect(rect, radius, radius, paint);

        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.GREEN);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawRoundRect(rect, radius, radius, boarderPaint);
        }
        return desBitmap;
    }


}

