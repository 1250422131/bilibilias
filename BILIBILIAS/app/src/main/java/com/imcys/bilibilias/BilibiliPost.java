package com.imcys.bilibilias;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class BilibiliPost {

    private static JSONObject JsonStr;

    /**
     * 点赞操作
     * @param Bvid 待演算视频ID
     * @param toKen 鉴权值
     * @param csrf 复核值
     * @return 判断值
     */
    public static String Like(String Bvid, String toKen,String csrf) {
        if (isENChar(Bvid)) {
            if (Bvid.contains("av")) {
                Bvid = Bvid.replaceAll("av", "");
                String likeStr = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like","aid="+ Bvid +"&like=1&csrf="+csrf,toKen);
                System.out.println(likeStr);
                String pd = null;
                try {
                    JsonStr = new JSONObject(likeStr);
                    pd = JsonStr.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return pd;
            }else{
                String likeStr = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like","bvid="+ Bvid +"&like=1&csrf="+csrf,toKen);
                System.out.println(likeStr);
                String pd = null;
                try {
                    JsonStr = new JSONObject(likeStr);
                    pd = JsonStr.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return pd;
            }
        }else{
            String likeStr = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like","aid="+ Bvid +"&like=1&csrf="+csrf,toKen);
            System.out.println(likeStr);
            String pd = null;
            try {
                JsonStr = new JSONObject(likeStr);
                pd = JsonStr.getString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pd;
        }
    }

    /**
     * 视频投币方法
     * @param Bvid 待演算视频ID
     * @param multiply 投币数量 上限为1
     * @param toKen 鉴权值
     * @param csrf 复核值
     * @return 判断值
     */
    public static String add(String Bvid, String multiply, String toKen,String csrf){
        if (isENChar(Bvid)) {
            if (Bvid.contains("av")) {
                Bvid = Bvid.replaceAll("av", "");
                String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/coin/add","aid="+ Bvid +"&multiply="+multiply+"&csrf="+csrf,toKen);
                System.out.println(Str);
                String pd = null;
                try {
                    JsonStr = new JSONObject(Str);
                    pd = JsonStr.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return pd;
            }else{
                String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/coin/add","bvid="+ Bvid +"&multiply="+multiply+"&csrf="+csrf,toKen);
                System.out.println(Str);
                String pd = null;
                try {
                    JsonStr = new JSONObject(Str);
                    pd = JsonStr.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return pd;
            }
        }else{
            String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/coin/add","aid="+ Bvid +"&multiply="+multiply+"&csrf="+csrf,toKen);
            System.out.println(Str);
            String pd = null;
            try {
                JsonStr = new JSONObject(Str);
                pd = JsonStr.getString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pd;
        }
    }

    /**
     * 三连方法
     * @param Bvid 待演算视频ID
     * @param toKen 鉴权值
     * @param csrf 复核值
     * @return 判断值
     */
    public static String triple(String Bvid, String toKen,String csrf){
        if (isENChar(Bvid)) {
            if (Bvid.contains("av")) {
                Bvid = Bvid.replaceAll("av", "");
                String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like/triple","aid="+ Bvid +"&csrf="+csrf,toKen);
                System.out.println(Str);
                String pd = null;
                try {
                    JsonStr = new JSONObject(Str);
                    pd = JsonStr.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return pd;
            }else{
                String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like/triple","bvid="+ Bvid +"&csrf="+csrf,toKen);
                System.out.println(Str);
                String pd = null;
                try {
                    JsonStr = new JSONObject(Str);
                    pd = JsonStr.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return pd;
            }
        }else{
            String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like/triple","aid="+ Bvid +"&csrf="+csrf,toKen);
            System.out.println(Str);
            String pd = null;
            try {
                JsonStr = new JSONObject(Str);
                pd = JsonStr.getString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pd;
        }
    }

    public static String nav(String toKen){
        String Str = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/nav",toKen);
        System.out.println(Str);
        String pd = null;
        try {
            JsonStr = new JSONObject(Str);
            pd = JsonStr.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pd;
    }


    //下面是文件读写删改

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }


    public static String fileRead(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        // read
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String str = br.readLine();
        return str;

    }


    public static void fileWrite(String path,String Str) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Str);
        bw.flush();
        bw.close();
        fw.close();
    }


    //bv和av号的检测 -- 正则表达匹配是否有英文
    public static boolean isENChar(String string) {
        //赋值初始类型
        boolean flag = false;
        //正则表达式执行返回结果
        Pattern p = Pattern.compile("[a-zA-z]");
        //判断是否满足matcher()是完全匹配find()是部分满足
        if(p.matcher(string).find()) {
            flag = true;
        }
        return flag;
    }

    //截取字符串方法
    public static String sj(String str, String start, String end)
    {
        if (str.contains(start) && str.contains(end))
        {
            str = str.substring(str.indexOf(start) + start.length());
            return str.substring(0, str.indexOf(end));
        }
        return "";
    }

    //图片加载方法
    public static Bitmap returnBitMap(String url) {
        java.net.URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



}
