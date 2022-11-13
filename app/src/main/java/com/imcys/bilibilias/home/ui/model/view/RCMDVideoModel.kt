package com.imcys.bilibilias.home.ui.model.view

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.utils.HttpUtils

class RCMDVideoModel {

    lateinit var context: Context

    fun likeVideo(view: View, bvid: String) {
        Log.e("Tag","点击了")
        HttpUtils()
            .addHeader("cookie", App.cookies)
            .addParam("bvid", bvid)
            .addParam("like", "1")
            .addParam("csrf", App.biliJct)
            .post(BilibiliApi().likeVideoPath, LikeVideoBean::class.java) {
                App.handler.post {
                    if (it.code == 0) {
                        Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "点赞失败，错误码 ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

}