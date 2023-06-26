package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemUserWorksBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.UserWorksBean

class UserWorksAdapter : ListAdapter<UserWorksBean.DataBean.ListBean.VlistBean, ViewHolder>(object :
    DiffUtil.ItemCallback<UserWorksBean.DataBean.ListBean.VlistBean>() {
    override fun areItemsTheSame(
        oldItem: UserWorksBean.DataBean.ListBean.VlistBean,
        newItem: UserWorksBean.DataBean.ListBean.VlistBean,
    ): Boolean {
        return oldItem.aid == newItem.aid
    }

    override fun areContentsTheSame(
        oldItem: UserWorksBean.DataBean.ListBean.VlistBean,
        newItem: UserWorksBean.DataBean.ListBean.VlistBean,
    ): Boolean {
        return oldItem.bvid == newItem.bvid
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemUserWorksBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_user_works, parent, false
            )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemUserWorksBinding>(holder.itemView)


        binding?.apply {
            vlistBean = getItem(position)
            holder.itemView.setOnClickListener {
                AsVideoActivity.actionStart(root.context, getItem(position).bvid)
            }

        }

    }
}