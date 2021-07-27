package com.imcys.bilibilias.about;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.user.UserActivity;


import java.util.List;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ViewHolder> {

    private final List<Support> mSupportList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.support_item, parent, false);
        final SupportAdapter.ViewHolder holder = new SupportAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Support support = mSupportList.get(position);
        Glide.with(support.getContext())
                .load(support.getUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.FaceImage);

        holder.NameText.setText(support.getName());
        holder.ModeText.setText(support.getMode());
    }

    @Override
    public int getItemCount() {
        return mSupportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //变量声明
        View View;
        ImageView FaceImage;
        TextView NameText;
        TextView ModeText;


        public ViewHolder(View view) {
            //布局绑定
            super(view);
            View = view;
            FaceImage = (ImageView) view.findViewById(R.id.support_Face);
            NameText = (TextView) view.findViewById(R.id.support_Name);
            ModeText = (TextView) view.findViewById(R.id.support_Mode);
        }

    }

    public SupportAdapter(List<Support> supportList) {
        mSupportList = supportList;
    }


}

class Support {
    private Context context;
    private String Name;
    private String Mode;
    private String Url;

    public Support(String Name, String Mode, String Url, Context context) {
        this.Mode = Mode;
        this.Name = Name;
        this.context = context;
        this.Url = Url;
    }

    public Context getContext() {
        return context;
    }

    public String getName() {
        return Name;
    }

    public String getMode() {
        return Mode;
    }

    public String getUrl() {
        return Url;
    }


}