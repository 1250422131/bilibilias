package com.imcys.bilibilias;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class BilibiliPost {

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
                String pd = sj(likeStr,"\"code\"",",");
                return pd;
            }else{
                String likeStr = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like","bvid="+ Bvid +"&like=1&csrf="+csrf,toKen);
                System.out.println(likeStr);
                String pd = sj(likeStr,"\"code\":",",");
                return pd;
            }
        }else{
            String likeStr = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like","aid="+ Bvid +"&like=1&csrf="+csrf,toKen);
            System.out.println(likeStr);
            String pd = sj(likeStr,"\"code\"",",");
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
                String pd = sj(Str,"\"code\"",",");
                return pd;
            }else{
                String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/coin/add","bvid="+ Bvid +"&multiply="+multiply+"&csrf="+csrf,toKen);
                System.out.println(Str);
                String pd = sj(Str,"\"code\":",",");
                return pd;
            }
        }else{
            String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/coin/add","aid="+ Bvid +"&multiply="+multiply+"&csrf="+csrf,toKen);
            System.out.println(Str);
            String pd = sj(Str,"\"code\"",",");
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
                String pd = sj(Str,"\"code\"",",");
                return pd;
            }else{
                String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like/triple","bvid="+ Bvid +"&csrf="+csrf,toKen);
                System.out.println(Str);
                String pd = sj(Str,"\"code\":",",");
                return pd;
            }
        }else{
            String Str = HttpUtils.doPost("http://api.bilibili.com/x/web-interface/archive/like/triple","aid="+ Bvid +"&csrf="+csrf,toKen);
            System.out.println(Str);
            String pd = sj(Str,"\"code\"",",");
            return pd;
        }
    }

    public static String nav(String toKen){
        String Str = HttpUtils.doGet("http://api.bilibili.com/x/web-interface/nav",toKen);
        System.out.println(Str);
        String pd = sj(Str,"\"code\":",",");
        return pd;
    }


    //下面是文件读写
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
}
