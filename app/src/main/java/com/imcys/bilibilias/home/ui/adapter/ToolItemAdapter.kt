package com.imcys.bilibilias.home.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemToolBinding
import com.imcys.bilibilias.databinding.ItemToolLiveCardBinding
import com.imcys.bilibilias.databinding.ItemToolVideoCardBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.model.ToolItemBean


class ToolItemAdapter : ListAdapter<ToolItemBean, ViewHolder>(
    object : DiffUtil.ItemCallback<ToolItemBean>() {
        override fun areItemsTheSame(oldItem: ToolItemBean, newItem: ToolItemBean): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ToolItemBean, newItem: ToolItemBean): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = when (viewType) {
            0 -> {
                DataBindingUtil.inflate<ItemToolBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_tool, parent, false
                )

            }

            1 -> {
                DataBindingUtil.inflate<ItemToolVideoCardBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_tool_video_card, parent, false
                )
            }

            2 -> {
                DataBindingUtil.inflate<ItemToolLiveCardBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_tool_live_card, parent, false
                )
            }

            else -> {
                TODO("无效内容")
            }
        }

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItem(position).type) {
            0 -> {
                //普通item
                val binding = DataBindingUtil.getBinding<ItemToolBinding>(holder.itemView)
                binding?.toolItemBean = getItem(position)
                val clickEvent: () -> Unit = getItem(position).clickEvent
                holder.itemView.setOnClickListener {
                    clickEvent()
                }
            }

            1 -> {
                //视频/番剧Item
                val binding = DataBindingUtil.getBinding<ItemToolVideoCardBinding>(holder.itemView)
                binding?.videoBaseBean = getItem(position).videoBaseBean
                val clickEvent: () -> Unit = getItem(position).clickEvent
                binding?.root?.setOnClickListener {

                    val videoPic = androidx.core.util.Pair<View, String>(
                        binding.itemToolVideoCardPic,
                        "videoPic"
                    )

                    val i = Intent(holder.itemView.context, AsVideoActivity::class.java)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            holder.itemView.context as HomeActivity,
                            videoPic
                        )



                    i.putExtra("bvId", getItem(position).videoBaseBean?.data?.bvid)

                    holder.itemView.context.startActivity(i, optionsCompat.toBundle())

                    clickEvent()
                }
            }

            2 -> {
                //视频/番剧Item
                val binding = DataBindingUtil.getBinding<ItemToolLiveCardBinding>(holder.itemView)
                binding?.liveRoomDataBean = getItem(position).liveRoomDataBean
                val clickEvent: () -> Unit = getItem(position).clickEvent
                binding?.root?.setOnClickListener {
                    clickEvent()
                }
            }
        }


    }


}