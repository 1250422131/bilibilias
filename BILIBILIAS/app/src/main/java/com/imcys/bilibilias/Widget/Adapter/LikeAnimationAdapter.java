package com.imcys.bilibilias.Widget.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.user.UserVideo;

import java.util.List;

public class LikeAnimationAdapter extends ArrayAdapter<UserVideo> {
    private Context context;
    private List<UserVideo> UserVideoList;
    private int resourceID;

    public LikeAnimationAdapter(Context context, int textViewResourceId, List<UserVideo> UserVideoList) {
        super(context, textViewResourceId, UserVideoList);
        this.context = context;
        this.UserVideoList = UserVideoList;
        resourceID = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserVideo mUserVideo = getItem(position);//获得当前项fruit实例
        //动态加载布局文件
        View view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);

        ImageView WidgetImage = (ImageView) convertView.findViewById(R.id.Widget_Video_Image);
        TextView WidgetDm = (TextView) convertView.findViewById(R.id.Widget_Video_Dm);
        TextView WidgetPlay = (TextView) convertView.findViewById(R.id.Widget_Video_Play);
        TextView WidgetTitle = (TextView) convertView.findViewById(R.id.User_Video_Title);
        TextView WidgetAid = (TextView) convertView.findViewById(R.id.Widget_Video_Aid);
        TextView WidgetPic = (TextView) convertView.findViewById(R.id.Widget_Video_Pic);
        TextView WidgetBvid = (TextView) convertView.findViewById(R.id.Widget_Video_Bvid);


        WidgetTitle.setText(mUserVideo.getTitle());
        WidgetPlay.setText(mUserVideo.getPlay());
        WidgetDm.setText(mUserVideo.getDm());
        WidgetBvid.setText(mUserVideo.getBvid());
        WidgetPic.setText(mUserVideo.getPic());
        WidgetAid.setText(mUserVideo.getAid());
        Glide.with(mUserVideo.getContext()).load(mUserVideo.getPic()).into(WidgetImage);

        //返回布局
        return view;
    }


}


