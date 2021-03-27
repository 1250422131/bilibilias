package com.imcys.bilibilias.home;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.imcys.bilibilias.R;


import java.util.List;


public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {

    private List<Function> mFunctionList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        //变量声明
        View fruitView;
        ImageView functionImage;
        TextView functionTitle;


        public ViewHolder(View view) {
            //布局绑定
            super(view);
            fruitView = view;
            functionImage = (ImageView) view.findViewById(R.id.Home_FunctionImage);
            functionTitle = (TextView) view.findViewById(R.id.Home_FunctionTitle);
        }

    }

    public FunctionAdapter(List<Function> functionList) {
        mFunctionList = functionList;
    }


    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position,int tag);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //设置布局
        Function function = mFunctionList.get(position);
        holder.functionTitle.setText(function.getTitle());
        Glide.with(function.getContext()).load(function.getSrcUrl()).into(holder.functionImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position,function.getViewTag());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFunctionList.size();
    }
}