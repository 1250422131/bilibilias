package com.imcys.bilibilias.as;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.imcys.bilibilias.R;

import java.util.List;


public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {

    private List<Reply> mReplyList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView replyImage;
        TextView replyName;
        TextView replyStr;
        View replyView;

        public ViewHolder(View view) {
            super(view);
            replyView = view;
            replyImage = (ImageView) view.findViewById(R.id.As_Item_ImageView);
            replyName = (TextView) view.findViewById(R.id.As_Item_UpName);
            replyStr = (TextView) view.findViewById(R.id.As_Item_ReplyStr);
        }

    }

    public ReplyAdapter(List<Reply> replyList) {
        mReplyList = replyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reply reply = mReplyList.get(position);
        Glide.with(reply.getContext()).load(reply.getUrl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(holder.replyImage);
        holder.replyName.setText(reply.getName());
        holder.replyStr.setText(reply.getReplyStr());
    }

    @Override
    public int getItemCount() {
        return mReplyList.size();
    }

    
}