package com.imcys.bilibilias.Widget.Adapter;

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


import android.app.PendingIntent;
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
import android.widget.RemoteViewsService.RemoteViewsFactory;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.imcys.bilibilias.user.UserVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyRemoteViewsFactory implements RemoteViewsFactory {

    private final Context mContext;
    public static List<String> mList = new ArrayList<String>();
    public static ArrayList<UserVideo> UserVideoList = new ArrayList<UserVideo>();
    private int width;
    private int height;

    /*
     * 构造函数
     */
    public MyRemoteViewsFactory(Context context, Intent intent) {
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
                String UserCollectionJson = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/history/cursor?max=0&view_at=0&business=", getCookie());
                try {
                    JSONObject UserCollectionData = new JSONObject(UserCollectionJson);
                    UserCollectionData = UserCollectionData.getJSONObject("data");
                    JSONArray UserCollectionArray = UserCollectionData.getJSONArray("list");
                    UserCollectionData = UserCollectionData.getJSONObject("cursor");
                    try {
                        UserVideoList.clear();
                        for (int i = 0; i < UserCollectionArray.length(); i++) {
                            JSONObject UserVideoJson = UserCollectionArray.getJSONObject(i);
                            JSONObject UserVideoCntInfo = UserVideoJson.getJSONObject("history");
                            String Title = UserVideoJson.getString("title");
                            String pic = UserVideoJson.getString("cover");
                            String play = "未知";
                            String Dm = "未知";
                            String bvid = UserVideoCntInfo.getString("bvid");
                            String aid = UserVideoCntInfo.getString("oid");
                            UserVideoList.add(new UserVideo(Title, bvid, aid, pic, play, Dm, mContext));

                        }
                        Log.i("BILIBILIAS", "余量" + UserVideoList.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
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

        UserVideo content = UserVideoList.get(position);
        // 创建在当前索引位置要显示的View
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_widget_video);

        // 设置要显示的内容 ImageView WidgetImage = (ImageView) convertView.findViewById(R.id.Widget_Video_Image);
        //        TextView WidgetDm = (TextView) convertView.findViewById(R.id.Widget_Video_Dm);
        //        TextView WidgetPlay = (TextView) convertView.findViewById(R.id.Widget_Video_Play);
        //        TextView WidgetTitle = (TextView) convertView.findViewById(R.id.User_Video_Title);
        //        TextView WidgetAid = (TextView) convertView.findViewById(R.id.Widget_Video_Aid);
        //        TextView WidgetPic = (TextView) convertView.findViewById(R.id.Widget_Video_Pic);
        //        TextView WidgetBvid = (TextView) convertView.findViewById(R.id.Widget_Video_Bvid);

        Bitmap bitmap = getImageBitmap(content.getPic());
        try {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            height = 1920;
            width = 1080;
        }


        rv.setImageViewBitmap(R.id.Widget_Video_Image, getRoundBitmapByShader(bitmap, width, height, 30, 0));
        rv.setTextViewText(R.id.Widget_Video_Dm, content.getDm());
        rv.setTextViewText(R.id.Widget_Video_Play, content.getPlay());
        rv.setTextViewText(R.id.Widget_Video_Title, content.getTitle());
        rv.setTextViewText(R.id.Widget_Video_Aid, content.getAid());
        rv.setTextViewText(R.id.Widget_Video_Bvid, content.getBvid());
        //填充Intent，填充在AppWdigetProvider中创建的PendingIntent
        Intent intent = new Intent();
        //传入点击行的数据
        intent.putExtra("content", content.getBvid());
        rv.setOnClickFillInIntent(R.id.Widget_Video_LinearLayout, intent);


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