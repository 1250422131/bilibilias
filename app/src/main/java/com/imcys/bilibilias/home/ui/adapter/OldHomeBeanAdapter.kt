package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.ItemHomeBannerBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.OldHomeBannerDataBean
import com.youth.banner.adapter.BannerAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class OldHomeBeanAdapter(
    private val datas: MutableList<String>,
    private val sumData: OldHomeBannerDataBean,
) :
    BannerAdapter<String, ViewHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemHomeBannerBinding>(
                LayoutInflater.from(parent?.context),
                R.layout.item_home_banner, parent, false
            )
        return ViewHolder(binding.root)
    }

    @SuppressLint("CheckResult")
    override fun onBindView(holder: ViewHolder?, data: String?, position: Int, size: Int) {
        val binding = DataBindingUtil.getBinding<ItemHomeBannerBinding>(holder!!.itemView)?.apply {
            itemHomeBannerTitle.text = data
            Glide.with(itemHomeBannerImage.context).load(sumData.imgUrlList[position])
                .into(itemHomeBannerImage)

            holder.itemView.setOnClickListener {
                loadEvent(sumData.typeList[position], position, holder.itemView.context)
            }

        }
    }

    @SuppressLint("IntentReset")
    private fun loadEvent(s: String?, position: Int, context: Context) {
        var intent = Intent()
        when (s) {

            "goBilibili" -> {
                intent.type = "text/plain"
                intent.data = Uri.parse(sumData.dataList[position])
                intent.action = "android.intent.action.VIEW"
                context.startActivity(intent)
            }

            "goAs" -> {
                AsVideoActivity.actionStart(context, sumData.dataList[position])
            }

            "goUrl" -> {
                val uri = Uri.parse(sumData.dataList[position])
                intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)

            }

            "getBiliBili" -> {
                val successToast: String = sumData.successToast[position]
                val failToast: String = sumData.failToast[position]
                getRequestBiliBili(sumData.dataList[position], successToast, failToast, context)
            }

            "postBiliBili" -> {
                val successToast: String = sumData.successToast[position]
                val failToast: String = sumData.failToast[position]
                val postData: String = sumData.postData[position]
                val token = sumData.token[position]

                postRequestBiliBili(
                    sumData.dataList[position],
                    postData,
                    successToast,
                    failToast,
                    token,
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

            val cookie = BaseApplication.dataKv.decodeString("cookies", "").toString()
            val goUrlStr = HttpUtils.doCardPost(url, getPost, cookie)
            try {
                val goUrlJson = JSONObject(goUrlStr.toString())
                val code = goUrlJson.getInt("code")
                val message = goUrlJson.optString("message")
                (context as Activity).runOnUiThread {
                    if (code == 0) {
                        Toast.makeText(context, successToast + message, Toast.LENGTH_SHORT).show()
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

        val cookie = BaseApplication.dataKv.decodeString("cookies", "").toString()

        HttpUtils.addHeader("cookie", cookie).get(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, failToast, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val requestStr = response.body!!.string()
                val requestJson = JSONObject(requestStr)
                val code = requestJson.optInt("code")
                val message = requestJson.optString("message")

                if (code == 0) {
                    Toast.makeText(context, successToast + message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, failToast + message, Toast.LENGTH_SHORT).show()
                }
            }

        })

    }


}