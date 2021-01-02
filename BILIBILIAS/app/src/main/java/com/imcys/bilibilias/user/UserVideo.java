package com.imcys.bilibilias.user;

import android.content.Context;

public class UserVideo {
    private Context context;
    private String title;
    private String bvid;
    private String aid;
    private String pic;
    private String Dm;
    private String play;

    public UserVideo(String title, String bvid, String aid, String pic, String play, String Dm, Context context){
        this.title = title; //标题
        this.bvid = bvid;
        this.aid = aid;
        this.pic = pic; //封名链接
        this.Dm = Dm; //弹幕数量
        this.play = play; //播放量
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getDm() {
        return Dm;
    }

    public String getPlay() {
        return play;
    }

    public String getBvid() {
        return bvid;
    }

    public String getAid() {
        return aid;
    }

    public String getPic() {
        return pic;
    }

    public String getTitle() {
        return title;
    }

}
