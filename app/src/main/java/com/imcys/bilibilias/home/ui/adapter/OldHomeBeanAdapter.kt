package com.imcys.bilibilias.home.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.core.model.bilibilias.Banner
import com.imcys.bilibilias.databinding.ItemHomeBannerBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.youth.banner.adapter.BannerAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class OldHomeBeanAdapter(
    private val aggregatingData: Banner,
) : BannerAdapter<String, OldHomeBeanAdapter.BannerViewHolder>(aggregatingData.textList) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding =
            ItemHomeBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindView(holder: BannerViewHolder, data: String, position: Int, size: Int) {
        holder.bind(aggregatingData)
    }

    class BannerViewHolder(binding: ItemHomeBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        private val bannerTitle = binding.itemHomeBannerTitle
        private val bannerImage = binding.itemHomeBannerImage
        fun bind(aggregatingData: Banner) {
            val position = bindingAdapterPosition
            bannerTitle.text = aggregatingData.textList[position]
            bannerImage.load(aggregatingData.imgUrlList[position])
            itemView.setOnClickListener {
                setEvent(aggregatingData.typeList[position], position, it.context, aggregatingData)
            }
        }

        private fun setEvent(
            type: String,
            position: Int,
            context: Context,
            aggregatingData: Banner
        ) {
            when (type) {
                "goBilibili" -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        Uri.parse(aggregatingData.dataList[position]),
                        "text/plain"
                    )
                    context.startActivity(intent)
                }

                "goAs" -> {
                    AsVideoActivity.actionStart(context, aggregatingData.dataList[position])
                }

                "goUrl" -> {
                    val uri = Uri.parse(aggregatingData.dataList[position])
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }

                "getBiliBili" -> {
                    val successToast: String = aggregatingData.successToast[position]
                    val failToast: String = aggregatingData.failToast[position]
                    getRequestBiliBili(
                        aggregatingData.dataList[position],
                        successToast,
                        failToast,
                        context
                    )
                }

                "postBiliBili" -> {
                    val successToast: String = aggregatingData.successToast[position]
                    val failToast: String = aggregatingData.failToast[position]
                    val postData: String = aggregatingData.postData[position]
                    val token = aggregatingData.token[position]

                    postRequestBiliBili(
                        aggregatingData.dataList[position],
                        postData,
                        successToast,
                        failToast,
                        token.toInt(),
                        context
                    )
                }
            }
        }

        private fun postRequestBiliBili(
            url: String,
            postData: String,
            successToast: String,
            failToast: String,
            token: Int,
            context: Context,
        ) {
            Thread {
                var getPost: String = postData
                if (token == 1) {
                    val biliJct = BaseApplication.dataKv.decodeString("bili_kct", "")

                    getPost = getPost.replace("{token}", biliJct!!)
                    println(getPost)
                }

                val cookie = BaseApplication.dataKv.decodeString(COOKIES, "").toString()
                val goUrlStr = HttpUtils.doCardPost(url, getPost, cookie)
                try {
                    val goUrlJson = JSONObject(goUrlStr.toString())
                    val code = goUrlJson.getInt("code")
                    val message = goUrlJson.optString("message")
                    (context as Activity).runOnUiThread {
                        if (code == 0) {
                            Toast.makeText(context, successToast + message, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, failToast + message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }.start()
        }

        private fun getRequestBiliBili(
            url: String,
            successToast: String,
            failToast: String,
            context: Context,
        ) {
            val cookie = BaseApplication.dataKv.decodeString(COOKIES, "").toString()

            HttpUtils.addHeader(COOKIE, cookie).get(
                url,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Toast.makeText(context, failToast, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val requestStr = response.body!!.string()
                        val requestJson = JSONObject(requestStr)
                        val code = requestJson.optInt("code")
                        val message = requestJson.optString("message")

                        if (code == 0) {
                            Toast.makeText(context, successToast + message, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, failToast + message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}
