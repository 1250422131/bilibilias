package com.imcys.bilibilias.tool_livestream.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.view.AsJzvdStd
import com.imcys.bilibilias.common.base.view.JzbdStdInfo
import com.imcys.bilibilias.tool_livestream.R
import com.imcys.bilibilias.tool_livestream.base.BaseActivity
import com.imcys.bilibilias.tool_livestream.databinding.ActivityLiveStreamBinding
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.anno.RouterAnno

@RouterAnno(
    hostAndPath = ARouterAddress.LiveStreamActivity,
)
class LiveStreamActivity : BaseActivity() {

    lateinit var binding: ActivityLiveStreamBinding
    private lateinit var asJzvdStd: AsJzvdStd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(target = this)
        binding = DataBindingUtil.setContentView<ActivityLiveStreamBinding?>(this,
            R.layout.activity_live_stream).apply {

        }
        initView()
    }


    private fun initView() {
        binding.apply {
            //设置播放按钮功能
            liveStreamFaButton.setOnClickListener {
                when (asJzvdStd.state) {
                    Jzvd.STATE_NORMAL, Jzvd.STATE_AUTO_COMPLETE -> {
                        //播放视频
                        asJzvdStd.startVideo()
                    }
                    Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                        //恢复播放/暂停播放
                        asJzvdStd.startButton.performClick()
                    }
                }
            }
            //绑定播放器，弹幕控制器
            asJzvdStd = liveStreamAsJzvdStd

        }

        //真正调用饺子播放器设置视频数据
        setAsJzvdConfig("https://cn-sxxa-cu-02-06.bilivideo.com/live-bvc/988798/live_474267806_49497952_1500.flv?expires=1674485857&pt=&deadline=1674485857&len=0&oi=3026178251&platform=&qn=0&trid=1000947c0a6004a9449e9956b06976efe1ca&uipk=100&uipv=100&nbs=1&uparams=cdn,deadline,len,oi,platform,qn,trid,uipk,uipv,nbs&cdn=cn-gotcha01&upsig=bedccec28a704fd5317edd792588adbe&sk=4207df3de646838b084f14f252be3afff667c581244ae1db28bc7b2502fd9756&p2p_type=0&src=57345&sl=1&free_type=0&sid=cn-sxxa-cu-02-06&chash=1&sche=ban&score=15&pp=rtmp&source=one&trace=0&site=c2c03e3799c7c175c9cf5b38b5e9b0f1&order=1","")
     }
    /**
     * 设置饺子播放器参数配置
     * @param url String
     * @param title String
     */
    private fun setAsJzvdConfig(url: String, title: String) {
        //val map = LinkedHashMap<Any, Any>()
        //map["760P"] = url
        val jzDataSource = JZDataSource(url, title)

        jzDataSource.headerMap["Cookie"] = BaseApplication.cookies;
        jzDataSource.headerMap["Referer"] = "https://live.bilibili.com/"
        jzDataSource.headerMap["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0";

        asJzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)

        asJzvdStd.setPlayStateListener(object : JzbdStdInfo {

            override fun statePlaying(state: Int) {

            }

            override fun stopPlay(state: Int) {


            }

            override fun endPlay(state: Int) {
            }

            override fun seekBarStopTracking(state: Int) {
            }


        })

    }
}