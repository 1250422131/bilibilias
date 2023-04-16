package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.model.user.UserBiliBiliCookieModel
import com.imcys.bilibilias.databinding.ItemAsAccountBinding

class BiliBiliCookieAdapter(
    val mOnBindViewHolder: (binding: ItemAsAccountBinding, data: UserBiliBiliCookieModel.Data) -> Unit,
) : ListAdapter<UserBiliBiliCookieModel.Data, ViewHolder>(object :
    DiffUtil.ItemCallback<UserBiliBiliCookieModel.Data>() {
    override fun areItemsTheSame(
        oldItem: UserBiliBiliCookieModel.Data,
        newItem: UserBiliBiliCookieModel.Data,
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: UserBiliBiliCookieModel.Data,
        newItem: UserBiliBiliCookieModel.Data,
    ): Boolean {
        return oldItem.cookie == newItem.cookie
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemAsAccountBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_as_account, parent, false
            )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<ItemAsAccountBinding>(holder.itemView)
        binding?.apply {
            data = getItem(position)
            holder.itemView.setOnClickListener {
                mOnBindViewHolder(binding, getItem(position))

            }
        }

    }
}