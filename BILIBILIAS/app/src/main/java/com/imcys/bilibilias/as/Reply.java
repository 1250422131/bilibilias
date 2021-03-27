package com.imcys.bilibilias.as;

import android.content.Context;

public class Reply {

    private Context context;
    private String Name;
    private String Url;//头像url
    private String ReplyStr;

    public Reply(String Name,String ReplyStr,String Url,Context context){
        this.Name = Name;
        this.ReplyStr = ReplyStr;
        this.Url = Url;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getName() {
        return Name;
    }

    public String getUrl(){
        return Url;
    }

    public String getReplyStr(){
        return ReplyStr;
    }


}
