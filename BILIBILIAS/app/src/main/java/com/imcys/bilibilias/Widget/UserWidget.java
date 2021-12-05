package com.imcys.bilibilias.Widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;


import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;


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

public class UserWidget extends AppWidgetProvider {
    private String cookie;
    private int mid;
    private String face;
    private String name;
    private String money;
    private String sign;
    private String sex;
    private String UserMid;
    private String VipText;
    private String nickname_color;
    private int level;
    private int VipType;
    private String image_enhance;
    private String follower;
    private String following;
    private RemoteViews rv;
    private ComponentName cn;
    private AppWidgetManager manager;
    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @SuppressLint("RemoteViewLayout")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i("AppWidget", "开始了更新");
        rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_user);
        //这里获得当前的包名，并且用AppWidgetManager来向NewAppWidget.class发送广播。
        manager = AppWidgetManager.getInstance(context);
        cn = new ComponentName(context, UserWidget.class);
        cookie = getCookie();
        UserNav(context);
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
    @SuppressLint("RemoteViewLayout")
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Intent mTimerIntent = new Intent(context, WidgetService.class);
        // context.startService(mTimerIntent);
        rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_user);
        //这里获得当前的包名，并且用AppWidgetManager来向NewAppWidget.class发送广播。
        manager = AppWidgetManager.getInstance(context);
        cn = new ComponentName(context, UserWidget.class);
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

    public String getCookie() {
        String CookiePath = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/哔哩哔哩视频/cookie.txt";
        try {
            File file = new File(CookiePath);
            if(!file.exists()){
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void UserNav(Context context){
        new Thread(() -> {
            String UserNavStr = HttpUtils.doGet("https://api.bilibili.com/x/web-interface/nav",cookie);
            try {
                JSONObject UserNavJson = new JSONObject(UserNavStr);
                UserNavJson = UserNavJson.getJSONObject("data");
                mid = UserNavJson.getInt("mid");
                String UserInfo = HttpUtils.doGet("https://api.bilibili.com/x/space/acc/info?mid=" + mid, cookie);
                final String UserFansJson = HttpUtils.doGet("https://api.bilibili.com/x/relation/stat?vmid=" + mid, cookie);
                JSONObject UserFansData = new JSONObject(UserFansJson);
                UserFansData = UserFansData.getJSONObject("data");
                follower = UserFansData.getString("follower");
                following = UserFansData.getString("following");
                JSONObject UserNavInfo = new JSONObject(UserInfo);
                UserNavInfo = UserNavInfo.getJSONObject("data");
                JSONObject UserPendant = UserNavInfo.getJSONObject("pendant");
                image_enhance = UserPendant.getString("image_enhance");
                JSONObject UserVipData = UserNavInfo.getJSONObject("vip");
                VipType = UserVipData.getInt("type");
                level = UserNavInfo.getInt("level");
                face = UserNavInfo.getString("face");
                name = UserNavInfo.getString("name");
                money = UserNavInfo.getString("coins");
                sign = UserNavInfo.getString("sign");
                Bitmap mUserFace = getImageBitmap(face);
                mUserFace = getCircleBitmapByShader(mUserFace,750,750,0);
                Bitmap mTopUserFace = getImageBitmap("https://i.loli.net/2021/01/29/5R2AfE1u9rP7Qjx.jpg");
                mTopUserFace = rsBlur(context,mTopUserFace,5);
                mTopUserFace = getRoundBitmapByShader(mTopUserFace,518,193,20,0);
                if(!image_enhance.equals("")){
                    Bitmap mEnhanceFace = getImageBitmap(image_enhance);
                    rv.setImageViewBitmap(R.id.Widget_User_EnhanceFace,mEnhanceFace);
                }else{
                    Bitmap mEnhanceFace = getImageBitmap("http://i1.hdslb.com/bfs/garb/item/393eab6140d1e849ab4cd2c1bd0f75afe809f1f5.png");
                    rv.setImageViewBitmap(R.id.Widget_User_EnhanceFace,mEnhanceFace);
                }
                rv.setTextViewText(R.id.Widget_User_follower,""+follower);
                rv.setTextViewText(R.id.Widget_User_following,""+following);
                rv.setTextViewText(R.id.Widget_User_money,""+money);
                rv.setTextViewText(R.id.Widget_User_name,name);
                rv.setTextViewText(R.id.Widget_User_sign,sign);
                rv.setImageViewBitmap(R.id.Widget_User_Face,mUserFace);
                rv.setImageViewBitmap(R.id.Widget_User_BJ,mTopUserFace);
                manager.updateAppWidget(cn, rv);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bitmap rsBlur(Context context, Bitmap source, int radius){
        Bitmap inputBmp = source;
        //(1)
        RenderScript renderScript =  RenderScript.create(context);
        // Allocate memory for Renderscript to work with
        //(2)
        final Allocation input = Allocation.createFromBitmap(renderScript,inputBmp);
        final Allocation output = Allocation.createTyped(renderScript,input.getType());
        //(3)
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);
        //(8)
        renderScript.destroy();

        return inputBmp;
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

    //https://blog.csdn.net/ezconn/article/details/90298487 ← 来自网页


    /**
     * 通过BitmapShader实现圆形边框
     * @param bitmap
     * @param outWidth 输出的图片宽度
     * @param outHeight 输出的图片高度
     * @param radius 圆角大小
     * @param boarder 边框宽度
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

    /**
     * 通过BitmapShader实现圆形边框
     * @param bitmap
     * @param outWidth 输出的图片宽度
     * @param outHeight 输出的图片高度
     * @param boarder 边框大小
     */
    public static Bitmap getCircleBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int boarder) {
        int radius;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (outHeight > outWidth) {
            radius = outWidth / 2;
        } else {
            radius = outHeight / 2;
        }//创建canvas
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, paint);
        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.GREEN);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, boarderPaint);
        }
        return desBitmap;
    }


}

