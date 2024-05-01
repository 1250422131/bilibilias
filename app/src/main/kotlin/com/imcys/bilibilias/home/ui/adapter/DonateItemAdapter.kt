package com.imcys.bilibilias.home.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.ItemDonateDocBinding
import com.imcys.bilibilias.databinding.ItemDonatePayTypeBinding
import com.imcys.bilibilias.databinding.ItemDonateProgressBinding
import com.imcys.bilibilias.databinding.ItemTipBinding
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.imcys.bilibilias.home.ui.model.DonateViewBean
import javax.inject.Inject

class DonateItemAdapter @Inject constructor() :
    ListAdapter<DonateViewBean, ViewHolder>(object : DiffUtil.ItemCallback<DonateViewBean>() {
        override fun areItemsTheSame(oldItem: DonateViewBean, newItem: DonateViewBean): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: DonateViewBean, newItem: DonateViewBean): Boolean {
            return oldItem.type == newItem.type
        }

    }) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            DonateActivity.PAY_XML -> {

                DataBindingUtil.inflate<ItemDonatePayTypeBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_donate_pay_type, parent, false
                )
            }

            DonateActivity.PAY_PROGRESS -> {
                DataBindingUtil.inflate<ItemDonateProgressBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_donate_progress, parent, false
                )
            }

            DonateActivity.PAY_DOC -> {
                DataBindingUtil.inflate<ItemDonateDocBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_donate_doc, parent, false
                )
            }

            DonateActivity.PAY_TIP -> {
                DataBindingUtil.inflate<ItemTipBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_tip, parent, false
                )
            }

            else -> {
                TODO("触发意外事件")
            }
        }
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = when (getItem(position).type) {
            DonateActivity.PAY_XML -> {
                DataBindingUtil.getBinding<ItemDonatePayTypeBinding>(holder.itemView)?.apply {
                    itemDonateWechatPay.setOnClickListener {
                        DialogUtils.dialog(
                            holder.itemView.context,
                            "微信支付",
                            "感谢捐款支持",
                            "好嘞",
                            imageUrl = getItem(1).oldDonateBean?.weChat,
                            positiveButtonClickListener = {}
                        ).show()
                    }

                    itemDonatePayAlipay.setOnClickListener {
                        DialogUtils.dialog(
                            holder.itemView.context,
                            "支付宝支付",
                            "感谢捐款支持",
                            "好嘞",
                            imageUrl = getItem(1).oldDonateBean?.alipay,
                            positiveButtonClickListener = {}
                        ).show()
                    }
                }
            }

            DonateActivity.PAY_PROGRESS -> {
                DataBindingUtil.getBinding<ItemDonateProgressBinding>(holder.itemView)?.apply {
                    oldDonateBean = getItem(position).oldDonateBean
                    val surplus = oldDonateBean?.surplus?.toDouble()
                    val total = oldDonateBean?.total?.toDouble()
                    val progress = if (surplus == 0.00) {
                        0
                    } else {
                        ((surplus!! / total!!) * 100).toInt()
                    }
                    itemDownloadProgressBar.progress = progress
                }
            }

            DonateActivity.PAY_DOC -> {
                DataBindingUtil.getBinding<ItemDonateDocBinding>(holder.itemView)
            }

            DonateActivity.PAY_TIP -> {
                DataBindingUtil.getBinding<ItemTipBinding>(holder.itemView)?.apply {
                    tipBean = getItem(position)?.tipBean
                    Glide.with(tipFace).load(tipBean?.face).into(tipFace)
                    holder.itemView.setOnClickListener {
                        val intent = Intent(holder.itemView.context, tipBean?.activity)
                        holder.itemView.context.startActivity(intent)
                    }

                }

            }

            else -> {
                TODO("触发意外事件")
            }
        }
    }
}