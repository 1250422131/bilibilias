package com.imcys.bilibilias.user;

import static com.imcys.bilibilias.BilibiliPost.sj;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;


import java.util.List;

public class CacheAdapter extends RecyclerView.Adapter<CacheAdapter.ViewHolder> {
    private final List<Cache> mCache;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener main) {
        this.listener = main;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public CacheAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cache_item, parent, false);
        final CacheAdapter.ViewHolder holder = new CacheAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        Cache cache = mCache.get(position);
        switch (cache.getType()) {
            case "flv":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/uDp3LzbmEYPhXwT.png").into(holder.FaceImage);
                break;
            case "mp4":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/LHm6vCnE18kzlpt.png").into(holder.FaceImage);
                break;
            case "xml":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/dTlB8mLIbt3yw7c.png").into(holder.FaceImage);
                break;
            case "jpg":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/4tQUmGhRvXwgjly.png").into(holder.FaceImage);
                break;
            case "png":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/vqgEIfnDuwYjA8J.png").into(holder.FaceImage);
                break;
            case "jpeg":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/K5ObfdzZJ4STD8v.png").into(holder.FaceImage);
                break;
            case "txt":
                Glide.with(cache.getContext()).load("https://i.loli.net/2021/11/07/ENV5As2qtoymIcG.png").into(holder.FaceImage);
        }

        holder.NameText.setText(cache.getName());
        holder.SizeText.setText(cache.getSize());

    }

    @Override
    public int getItemCount() {
        return mCache.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView SizeText;
        //变量声明
        View View;
        ImageView FaceImage;
        TextView NameText;
        TextView TypeText;


        public ViewHolder(View view) {
            //布局绑定
            super(view);
            View = view;
            FaceImage = (ImageView) view.findViewById(R.id.cache_Face);
            NameText = (TextView) view.findViewById(R.id.cache_Name);
            SizeText = (TextView) view.findViewById(R.id.cache_Size);
        }

    }

    public CacheAdapter(List<Cache> cache) {
        mCache = cache;
    }


}

class Cache {
    private Context context;
    private String Name;
    private String Type;
    private String Path;
    private String Size;

    public Cache(String Name, String Type, String Size, String Path, Context context) {
        this.Name = Name;
        this.Type = Type;
        this.Path = Path;
        this.Size = Size;
        this.context = context;
    }

    public String getSize() {
        return Size;
    }

    public String getName() {
        return Name;
    }

    public String getPath() {
        return Path;
    }

    public String getType() {
        return Type;
    }

    public Context getContext() {
        return context;
    }
}