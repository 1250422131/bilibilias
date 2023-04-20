package com.imcys.bilibilias.home.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemFgUserCardDataBinding
import com.imcys.bilibilias.databinding.ItemFgUserFaceBinding
import com.imcys.bilibilias.databinding.ItemFgUserToolBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.fragment.UserFragment
import com.imcys.bilibilias.home.ui.model.UserViewItemBean
import com.imcys.bilibilias.home.ui.model.view.ItemFgUserToolViewModel

class UserDataAdapter : ListAdapter<UserViewItemBean, ViewHolder>(object :
    ItemCallback<UserViewItemBean>() {
    override fun areItemsTheSame(oldItem: UserViewItemBean, newItem: UserViewItemBean): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: UserViewItemBean, newItem: UserViewItemBean): Boolean {
        return oldItem.type == newItem.type
    }

}) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            1 -> {
                DataBindingUtil.inflate<ItemFgUserFaceBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_fg_user_face, parent, false
                )
            }
            2 -> {
                DataBindingUtil.inflate<ItemFgUserCardDataBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_fg_user_card_data, parent, false
                )
            }
            3 -> {
                DataBindingUtil.inflate<ItemFgUserToolBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_fg_user_tool, parent, false
                )
            }
            else -> {
                TODO("错误")
            }
        }
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = when (getItem(position).type) {
            1 -> {
                DataBindingUtil.getBinding<ItemFgUserFaceBinding>(holder.itemView)?.apply {
                    userBaseBean = getItem(position).userBaseBean
                    if (userBaseBean?.data?.vip?.status == 1) {
                        val nameColor: String = if (userBaseBean?.data?.vip?.nickname_color != "") {
                            userBaseBean?.data?.vip?.nickname_color!!
                        } else {
                            "#000000"
                        }
                        itemFgUserFaceNameText.setTextColor(Color.parseColor(nameColor))
                    }

                }
            }
            2 -> {
                DataBindingUtil.getBinding<ItemFgUserCardDataBinding>(holder.itemView)?.apply {
                    upStatBeam = getItem(position).upStatBeam
                    userCardBean = getItem(position).userCardBean
                }
            }
            3 -> {
                DataBindingUtil.getBinding<ItemFgUserToolBinding>(holder.itemView)?.apply {
                    //由于ItemFgUserTool 子属于 UserFragment 又依附于 HomeActivity 因此这么转换
                    itemFgUserToolViewModel =
                        ViewModelProvider(holder.itemView.context as HomeActivity)[ItemFgUserToolViewModel::class.java]
                }
            }
            else -> {
                TODO("错误")
            }
        }
    }


}