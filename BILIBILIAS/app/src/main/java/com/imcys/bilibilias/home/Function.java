package com.imcys.bilibilias.home;

import android.content.Context;

public class Function {

    private Context context;
    private String Title;
    private String SrcUrl;//图标
    private int ViewTag;//标签

    public Function(String Title,String SrcUrl,int ViewTag,Context context){
        this.Title = Title;
        this.SrcUrl = SrcUrl;
        this.ViewTag = ViewTag;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getTitle(){
        return Title;
    }

    public String getSrcUrl(){
        return SrcUrl;
    }

    public int getViewTag(){
        return ViewTag;
    }

}
