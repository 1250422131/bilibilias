        package com.imcys.bilibilias.user;


import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.VideoAsActivity;

import java.util.List;

import me.samlss.broccoli.Broccoli;
import me.samlss.broccoli.BroccoliGradientDrawable;
import me.samlss.broccoli.PlaceholderParameter;


        public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.ViewHolder> {

    private List<UserVideo> mFruitList;





    static class ViewHolder extends RecyclerView.ViewHolder {
        //变量声明
        View fruitView;
        ImageView fruitImage;
        TextView fruitTitle;
        TextView fruitAid;
        TextView fruitBvid;
        TextView fruitPic;
        TextView fruitPlay;
        TextView fruitDm;


        public ViewHolder(View view) {
            //布局绑定
            super(view);
            fruitView = view;
            fruitImage = (ImageView) view.findViewById(R.id.User_Video_Image);
            fruitTitle = (TextView) view.findViewById(R.id.User_Video_Title);
            fruitAid = (TextView) view.findViewById(R.id.User_Video_Aid);
            fruitBvid = (TextView) view.findViewById(R.id.User_Video_Bvid);
            fruitPic = (TextView) view.findViewById(R.id.User_Video_Pic);
            fruitPlay = (TextView) view.findViewById(R.id.User_Video_Play);
            fruitDm = (TextView) view.findViewById(R.id.User_Video_Dm);



            Broccoli broccoli = new Broccoli();
            broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                    .setView(fruitImage)
                    .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                            Color.parseColor("#CCCCCC"), 0, 1000, new LinearInterpolator()))
                    .build());
            broccoli.show();

        }

    }

    public UserVideoAdapter(List<UserVideo> fruitList) {
        mFruitList = fruitList;
    }






    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_video, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                UserVideo fruit = mFruitList.get(position);
                String toAS = fruit.getBvid();
                if(fruit.getBvid().equals("")){
                    toAS = fruit.getAid();
                }
                Intent intent= new Intent();
                intent.setClass(fruit.getContext(), VideoAsActivity.class);
                intent.putExtra("UserVideoAid",toAS);
                fruit.getContext().startActivity(intent);
                Toast.makeText(view.getContext(), toAS, Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //设置布局
        UserVideo fruit = mFruitList.get(position);
        Glide.with(fruit.getContext()).load(fruit.getPic()).into(holder.fruitImage);
        holder.fruitTitle.setText(fruit.getTitle());
        holder.fruitAid.setText(fruit.getAid());
        holder.fruitBvid.setText(fruit.getBvid());
        holder.fruitPic.setText(fruit.getPic());
        holder.fruitPlay.setText(fruit.getPlay());
        holder.fruitDm.setText(fruit.getDm());

    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}