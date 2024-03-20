package com.imcys.bilibilias.home.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.imcys.bilibilias.common.base.extend.toColorInt
import com.imcys.bilibilias.common.base.utils.NumberUtils
import com.imcys.bilibilias.databinding.ItemToolBinding
import com.imcys.bilibilias.databinding.ItemToolVideoCardBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.fragment.tool.ToolDataHolder
import com.imcys.bilibilias.home.ui.fragment.tool.ToolItem

class ToolItemAdapter : ListAdapter<ToolItem, RecyclerView.ViewHolder>(Diff) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = when (viewType) {
            ToolItem.VIEW_TYPE_TOOL -> {
                val binding = ItemToolBinding.inflate(LayoutInflater.from(parent.context))
                ToolViewHolder(binding)
            }

            ToolItem.VIEW_TYPE_VIEW -> {
                val binding = ItemToolVideoCardBinding.inflate(LayoutInflater.from(parent.context))
                VideoViewHolder(binding)
            }

            else -> throw IllegalArgumentException("参数异常")
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.type) {
            ToolItem.VIEW_TYPE_TOOL -> {
                (holder as ToolViewHolder).bind(holder, item)
            }

            ToolItem.VIEW_TYPE_VIEW -> {
                // 视频/番剧Item
                (holder as VideoViewHolder).bind(holder)
            }
        }
    }

    class ToolViewHolder(binding: ItemToolBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.toolImage
        private val text = binding.toolName
        fun bind(holder: ToolViewHolder, item: ToolItem) {
            text.text = item.title
            image.setBackgroundColor(item.color.toColorInt())
            image.load(item.imageUrl)
            holder.itemView.setOnClickListener {
                item.event(it.context)
            }
        }
    }

    class VideoViewHolder(binding: ItemToolVideoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val pic = binding.itemToolVideoCardPic
        private val title = binding.itemToolVideoCardTitle
        private val videoDesc = binding.videoDesc
        private val viewStat = binding.viewStat
        private val danmakuStat = binding.danmakuStat
        fun bind(holder: VideoViewHolder) {
            val bean = ToolDataHolder.videoBaseBean!!.data
            pic.load(bean.pic)
            title.text = bean.title
            videoDesc.text = bean.desc
            viewStat.text = NumberUtils.digitalConversion(bean.stat.view)
            danmakuStat.text = NumberUtils.digitalConversion(bean.stat.danmaku)
            holder.itemView.setOnClickListener {
                val i = Intent(holder.itemView.context, AsVideoActivity::class.java)
                holder.itemView.context.startActivity(i)
            }
        }
    }

    companion object {
        object Diff : androidx.recyclerview.widget.DiffUtil.ItemCallback<ToolItem>() {
            override fun areItemsTheSame(oldItem: ToolItem, newItem: ToolItem): Boolean {
                return oldItem.toolCode == newItem.toolCode
            }

            override fun areContentsTheSame(oldItem: ToolItem, newItem: ToolItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
