package com.imcys.bilibilias.home.ui.model.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.http.HttpUtils

class RCMDVideoModel {

    lateinit var context: Context

    fun likeVideo(bvid: String) {
        Log.e("Tag","点击了")
        HttpUtils.addHeader("cookie", "")
            .addParam("bvid", bvid)
            .addParam("like", "1")
            .addParam("csrf", "")
            .post(BilibiliApi.likeVideoPath, LikeVideoBean::class.java) {
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