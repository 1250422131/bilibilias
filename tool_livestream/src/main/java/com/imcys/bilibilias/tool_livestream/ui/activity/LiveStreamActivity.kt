package com.imcys.bilibilias.tool_livestream.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.view.AsJzvdStd
import com.imcys.bilibilias.common.base.view.JzbdStdInfo
import com.imcys.bilibilias.tool_livestream.R
import com.imcys.bilibilias.tool_livestream.base.BaseActivity
import com.imcys.bilibilias.tool_livestream.databinding.ActivityLiveStreamBinding
import com.imcys.bilibilias.tool_livestream.ui.model.LiveMasterUserBean
import com.imcys.bilibilias.tool_livestream.ui.model.LiveRoomDataBean
import com.imcys.bilibilias.tool_livestream.ui.model.RoomPlayUrlInfoBean
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RouterAnno(
    hostAndPath = ARouterAddress.LiveStreamActivity,
)
class LiveStreamActivity : BaseActivity() {

    //直播间ID
    @AttrValueAutowiredAnno
    lateinit var roomId: String

    lateinit var binding: ActivityLiveStreamBinding

    lateinit var liveRoomDataBean: LiveRoomDataBean
    lateinit var liveMasterUserBean: LiveMasterUserBean

    private lateinit var asJzvdStd: AsJzvdStd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Component.inject(target = this)

        binding = DataBindingUtil.setContentView<ActivityLiveStreamBinding?>(
            this,
            R.layout.activity_live_stream
        ).apply {
        }

        initView()
    }


    private fun initView() {
        bindingAsJzvdStd()
        loadLiveRoomData()

    }

    private fun bindingAsJzvdStd() {

        binding.apply {
            asJzvdStd = liveStreamAsJzvdStd
        }
    }

    /**
     * 加载直播间的数据情况
     */
    private fun loadLiveRoomData() {

        lifecycleScope.launch {

            //获取基本信息
            liveRoomDataBean = getRoomInfo()
            binding.liveRoomDataBean = liveRoomDataBean

            //获取直播用户信息
            liveMasterUserBean = getLiveMasterUserData(liveRoomDataBean.data.uid)
            binding.liveMasterUserBean = liveMasterUserBean

            val livePlayUrlInfoBean = getRoomPlayUrlInfo()

            //加载播放器数据
            loadLiveAsJzPlayer(
                livePlayUrlInfoBean
            )
        }

    }

    private fun loadLiveAsJzPlayer(livePlayUrlInfoBean: RoomPlayUrlInfoBean) {

        setAsJzvdConfig(
            livePlayUrlInfoBean.data.durl[0].url.replace("https", "http"),
            liveRoomDataBean.data.title
        )
        bindLiveStreamFaButtonEvent()

    }


    private suspend fun getRoomInfo(): LiveRoomDataBean {
        return withContext(lifecycleScope.coroutineContext) {
            HttpUtils.addHeader("cookie", asUser.cookie)
                .asyncGet(
                    "${BilibiliApi.liveRoomDataPath}?room_id=$roomId",
                    LiveRoomDataBean::class.java
                )
        }
    }

    private suspend fun getLiveMasterUserData(uid: Long): LiveMasterUserBean {
        return withContext(lifecycleScope.coroutineContext) {
            HttpUtils.addHeader("cookie", asUser.cookie)
                .asyncGet(
                    "${BilibiliApi.liveUserMasterInfo}?uid=$uid",
                    LiveMasterUserBean::class.java
                )
        }
    }

    private suspend fun getRoomPlayUrlInfo(): RoomPlayUrlInfoBean {
        return withContext(lifecycleScope.coroutineContext) {
            HttpUtils.addHeader("cookie", asUser.cookie)
                .asyncGet(
                    "${BilibiliApi.liveRoomPlayUrl}?cid=$roomId&qn=0&platform=h5",
                    RoomPlayUrlInfoBean::class.java
                )
        }
    }

    /**
     * 绑定播放按钮事件
     * 其目的是为了让点击播放按钮时可以和播放器联动
     */
    private fun bindLiveStreamFaButtonEvent() {
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

        jzDataSource.headerMap["Cookie"] = asUser.cookie
        jzDataSource.headerMap["Referer"] = "https://live.bilibili.com"
        jzDataSource.headerMap["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.76"

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

    override fun onResume() {
        super.onResume()
        //百度统计
        StatService.onResume(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        JzvdStd.releaseAllVideos()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (JzvdStd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        if (asJzvdStd.state == Jzvd.STATE_PLAYING) { //暂停视频
            asJzvdStd.startButton.performClick()
        }
        //百度统计
        StatService.onPause(this)
    }


    companion object {
        fun actionStart(context: Context, roomId: String) {
            Router
                .with(context)
                .hostAndPath(ARouterAddress.LiveStreamActivity)
                .putString("roomId", roomId)
                .forward()

        }
    }
}