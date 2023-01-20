package com.imcys.bilibilias.home.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.view.AsJzvdStd
import com.imcys.bilibilias.base.view.JzbdStdInfo
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.VideoNumConversion
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.danmaku.BiliDanmukuParser
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.adapter.BangumiSubsectionAdapter
import com.imcys.bilibilias.home.ui.adapter.SubsectionAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.home.ui.model.view.AsVideoViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.loader.IllegalDataException
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.*
import java.util.zip.Inflater


class AsVideoActivity : BaseActivity() {


    //视频基本数据类，方便全局调用
    private lateinit var videoDataBean: VideoBaseBean

    lateinit var binding: ActivityAsVideoBinding

    //饺子播放器，方便全局调用
    private lateinit var asJzvdStd: AsJzvdStd

    //烈焰弹幕使 弹幕解析器
    private lateinit var asDanmaku: IDanmakuView

    //烈焰弹幕使，方便全局调用
    private lateinit var danmakuParser: BaseDanmakuParser
    private val danmakuContext = DanmakuContext.create()

    lateinit var userBaseBean: UserBaseBean

    //视频临时数据，方便及时调用，此方案考虑废弃
    var bvid: String = ""
    var avid: Int = 0
    var cid: Int = 0
    var epid: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_as_video)

        loadUserData()
        //加载视频首要信息
        initVideoData()
        //加载控件
        initView()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            userBaseBean = withContext(lifecycleScope.coroutineContext) { getUserData() }
        }
    }


    private fun initView() {


        binding.apply {

            //绑定播放器，弹幕控制器
            asJzvdStd = asVideoAsJzvdStd
            asDanmaku = asVideoAsJzvdStd.asDanmaku

            //设置播放按钮功能
            asVideoFaButton.setOnClickListener {
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


            //设置点击事件->这里将点击事件都放这个类了


            asVideoViewModel = AsVideoViewModel(this@AsVideoActivity, this)


        }
    }


    /**
     * 加载视频播放信息
     */
    private fun loadVideoPlay(type: String) {

        //这里默认用目前flv最高免费画质1080P，注意：flv已经被B站弃用，目前只能使用360P和1080P，后面得考虑想办法做音频分离。

        when (type) {
            "video" -> {
                HttpUtils.addHeader("cookie", BaseApplication.cookies)
                    .addHeader("referer", "https://www.bilibili.com")
                    .get("${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0&fourk=1",
                        VideoPlayBean::class.java) {
                        //设置布局视频播放数据
                        binding.videoPlayBean = it
                        //真正调用饺子播放器设置视频数据
                        setAsJzvdConfig(it.data.durl[0].url, "")
                        binding.asVideoCd.visibility = View.VISIBLE
                        binding.asVideoBangumiCd.visibility = View.GONE
                    }
            }
            "bangumi" -> {
                HttpUtils.addHeader("cookie", BaseApplication.cookies)
                    .addHeader("referer", "https://www.bilibili.com")
                    .get("${BaseApplication.roamApi}pgc/player/web/playurl?ep_id=$epid&qn=64&fnval=0&fourk=1",
                        BangumiPlayBean::class.java) {

                        //设置布局视频播放数据
                        binding.bangumiPlayBean = it
                        //真正调用饺子播放器设置视频数据
                        setAsJzvdConfig(it.result.durl[0].url, "")
                    }
            }
            else -> "${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0&fourk=1"
        }


    }


    /**
     * 加载视频数据
     */
    private fun initVideoData() {

        //这里必须通过外界获取数据
        val intent = intent

        val bvId = intent.getStringExtra("bvId")

        //这里才是真正的视频基本数据获取
        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get(BilibiliApi.getVideoDataPath + "?bvid=$bvId", VideoBaseBean::class.java) {
                //设置数据
                videoDataBean = it
                //这里需要显示视频数据
                showVideoData()
                //TODO 设置基本数据，注意这里必须优先，因为我们在后面会复用这些数据
                setBaseData(it)
                //加载用户卡片
                loadUserCardData(it.data.owner.mid)
                //加载弹幕信息
                loadDanmakuFlameMaster()
                //加载视频列表信息，这里判断下是不是番剧

                it.data.redirect_url?.apply {

                    //通过正则表达式检查该视频是不是番剧
                    val epRegex = Regex("""(?<=ep)([0-9]*)""")
                    if (epRegex.containsMatchIn(this)) {
                        //加载番剧视频列表
                        epid = epRegex.find(this)?.value?.toLong()!!
                        loadBangumiVideoList()
                    }

                } ?: apply {
                    //加载视频播放信息
                    loadVideoPlay("video")
                    loadVideoList() //加载正常列表
                }
                //检查三连情况
                archiveHasLikeTriple()


            }
    }

    /**
     * 检查三连情况
     */
    private fun archiveHasLikeTriple() {
        archiveHasLike()
        archiveCoins()
        archiveFavoured()
    }

    /**
     * 收藏检验
     */
    private fun archiveFavoured() {
        HttpUtils.get("${BilibiliApi.archiveFavoured}?aid=${bvid}",
            ArchiveFavouredBean::class.java) {
            binding.archiveFavouredBean = it
        }
    }

    /**
     * 检验投币情况
     */
    private fun archiveCoins() {
        HttpUtils.get("${BilibiliApi.archiveHasLikePath}?bvid=${bvid}",
            ArchiveHasLikeBean::class.java) {
            binding.archiveHasLikeBean = it
        }
    }

    /**
     * 检验是否点赞
     */
    private fun archiveHasLike() {
        HttpUtils.get("${BilibiliApi.archiveCoinsPath}?bvid=${bvid}",
            ArchiveCoinsBean::class.java) {
            binding.archiveCoinsBean = it
        }
    }

    /**
     * 加载番剧视频列表信息
     *
     */
    private fun loadBangumiVideoList() {
        HttpUtils.apply {
            if (BaseApplication.sharedPreferences.getBoolean("use_roam_cookie_state",
                    true)
            ) this.addHeader("cookie", BaseApplication.cookies)
        }.get(BaseApplication.roamApi + "pgc/view/web/season?ep_id=" + epid,
            BangumiSeasonBean::class.java) {


            isMember(it)


            binding.apply {

                if (it.result.episodes.size == 1) asVideoSubsectionRv.visibility = View.GONE

                //到这里就毋庸置疑的说，是番剧，要单独加载番剧缓存。
                binding.asVideoBangumiCd.visibility = View.VISIBLE
                binding.asVideoCd.visibility = View.GONE

                binding.bangumiSeasonBean = it
                asVideoSubsectionRv.adapter =
                    BangumiSubsectionAdapter(it.result.episodes.toMutableList(),
                        cid) { data, position ->
                        updateBangumiInformation(data, position)
                    }

                asVideoSubsectionRv.layoutManager =
                    LinearLayoutManager(this@AsVideoActivity, LinearLayoutManager.HORIZONTAL, false)

            }
        }

    }


    /**
     * 更新番剧信息
     * @param data EpisodesBean
     * @param position Int
     */
    private fun updateBangumiInformation(
        data: BangumiSeasonBean.ResultBean.EpisodesBean,
        position: Int,
    ) {
        val userVipState = userBaseBean.data.vip.status
        if (data.badge == "会员" && userVipState != 1) {
            DialogUtils.dialog(
                this,
                "越界啦",
                "没大会员就要止步于此了哦，切换到不需要大会员的子集或者视频吧。",
                "我知道啦",
                positiveButtonClickListener = {
                }
            ).show()
        } else {
            //更新CID刷新播放页面
            cid = data.cid
            epid = data.id.toLong()
            asJzvdStd.updatePoster(data.cover)

            //暂停播放
            changeFaButtonToPlay()
            //清空弹幕
            asDanmaku.release()
            //刷新播放器
            loadVideoPlay("bangumi")
            //更新弹幕
            loadDanmakuFlameMaster()
        }

    }

    private fun isMember(bangumiSeasonBean: BangumiSeasonBean) {
        var memberType = false

        val userVipState = userBaseBean.data.vip.status
        bangumiSeasonBean.result.episodes.forEach {
            if (it.cid == cid && it.badge == "会员" && userVipState != 1) memberType = true
        }
        if (memberType) {
            DialogUtils.dialog(
                this,
                "越界啦",
                "没大会员就要止步于此了哦，切换到不需要大会员的子集或者视频吧。",
                "我知道啦",
                positiveButtonClickListener = {
                }
            ).show()
        } else {
            loadVideoPlay("bangumi")
        }
    }


    /**
     * 获取用户基础信息
     * @return UserBaseBean
     */
    private suspend fun getUserData(): UserBaseBean {
        return HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .asyncGet("${BilibiliApi.userBaseDataPath}?mid=${BaseApplication.mid}",
                UserBaseBean::class.java)
    }


    /**
     * 加载视频列表信息
     *
     */
    private fun loadVideoList() {
        HttpUtils.get(BilibiliApi.videoPageListPath + "?bvid=" + bvid,
            VideoPageListData::class.java) {
            binding.apply {

                if (it.data.size == 1) asVideoSubsectionRv.visibility = View.GONE

                binding.videoPageListData = it
                asVideoSubsectionRv.adapter =
                    SubsectionAdapter(it.data.toMutableList()) { data, position ->
                        //更新CID刷新播放页面
                        cid = data.cid
                        //暂停播放
                        changeFaButtonToPlay()
                        //刷新播放器
                        loadVideoPlay("video")
                        //清空弹幕
                        asDanmaku.release()
                        //更新弹幕
                        loadDanmakuFlameMaster()
                    }

                asVideoSubsectionRv.layoutManager =
                    LinearLayoutManager(this@AsVideoActivity, LinearLayoutManager.HORIZONTAL, false)


            }
        }

    }

    /**
     * 写入基本变量数据
     * @param videoBaseBean VideoBaseBean
     */
    private fun setBaseData(videoBaseBean: VideoBaseBean) {
        bvid = videoBaseBean.data.bvid
        avid = videoBaseBean.data.aid
        cid = videoBaseBean.data.cid
        binding.videoBaseBean = videoBaseBean
    }


    /**
     * 加载弹幕信息
     */
    private fun loadDanmakuFlameMaster() {

        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get("${BilibiliApi.videoDanMuPath}?oid=$cid",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {

                        App.handler.post {
                            //储存弹幕
                            saveDanmaku(response.body!!.bytes())
                            //初始化弹幕配置
                            initDanmaku()
                        }
                    }

                })
    }


    private fun initDanmaku() {
        val input: InputStream =
            FileInputStream(getExternalFilesDir("temp").toString() + "/tempDm.xml")

        //解析弹幕
        danmakuParser = createParser(input)!!


        //设置弹幕配置
        setDanmakuContextCongif()

        //设置弹幕监听器
        setAsDanmakuCallback()

    }


    private fun loadUserCardData(mid: Long) {
        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get(BilibiliApi.getUserCardPath + "?mid=$mid", UserCardBean::class.java) {
                showUserCard()
                binding.userCardBean = it
            }

    }

    /**
     * 显示用户卡片
     */
    private fun showUserCard() {
        binding.apply {
            asVideoUserCardLy.visibility = View.VISIBLE
        }
    }

    /**
     * 显示视频数据页面
     */
    private fun showVideoData() {
        binding.apply {
            asVideoDataLy.visibility = View.VISIBLE
        }
    }


    override fun onBackPressed() {
        if (JzvdStd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        asDanmaku.apply {
            pause()
            // hide()
        }
        if (asJzvdStd.state == Jzvd.STATE_PLAYING) { //暂停视频
            asJzvdStd.startButton.performClick()
            changeFaButtonToPlay()
        }
        //百度统计
        StatService.onPause(this)


    }


    companion object {

        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }

        @Deprecated("B站已经在弱化aid的使用，我们不确定这是否会被弃用，因此这个方法将无法确定时效性")
        fun actionStart(context: Context, aid: Int) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", VideoNumConversion.toBvidOffline(aid))
            context.startActivity(intent)
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

        jzDataSource.headerMap["Cookie"] = BaseApplication.cookies;
        jzDataSource.headerMap["Referer"] = "https://www.bilibili.com/video/$bvid";
        jzDataSource.headerMap["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0";

        asJzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)

        asJzvdStd.setPlayStateListener(object : JzbdStdInfo {

            override fun statePlaying(state: Int) {

                changeFaButtonToPause()

                asDanmaku.apply {

                    if (state == Jzvd.STATE_PAUSE) {
                        //判断暂停的事时间是不是不等同于现在的播放时间
                        //如果不是相当于重新播放
                        if (asJzvdStd.stopTime != asJzvdStd.currentPositionWhenPlaying) {
                            start(asJzvdStd.currentPositionWhenPlaying)
                        } else {
                            resume()
                        }

                    } else {
                        prepare(danmakuParser, danmakuContext)
                        enableDanmakuDrawingCache(true)
                    }
                }

            }

            override fun stopPlay(state: Int) {
                changeFaButtonToPlay()
                //暂停弹幕
                asDanmaku.pause()


            }

            override fun endPlay(state: Int) {
                changeFaButtonToRedo()
            }

            override fun seekBarStopTracking(state: Int) {
                if (state == Jzvd.STATE_PLAYING) {
                    asDanmaku.start(asJzvdStd.currentPositionWhenPlaying)
                }
            }


        })

    }


    //——————————————————————————————————————————————————————————————————————————
    //悬浮按钮状态更新
    private fun changeFaButtonToPlay() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_play)
    }

    private fun changeFaButtonToPause() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_pause)
    }

    private fun changeFaButtonToRedo() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_redo)
    }


    private fun getEmphasizeColor(): ColorStateList {
        return ColorStateList.valueOf(Color.parseColor("#FB7299"))
    }

    //——————————————————————————————————————————————————————————————————————————


    //——————————————————————————————————————————————————————————————————————————
    //弹幕抽离
    /**
     * 储存弹幕内容
     */
    private fun saveDanmaku(bytes: ByteArray) {

        val bufferedSink: BufferedSink?
        val dest = File(getExternalFilesDir("temp").toString(), "tempDm.xml")
        if (!dest.exists()) dest.createNewFile()
        val sink = dest.sink() //打开目标文件路径的sink
        val decompressBytes =
            decompress(bytes) //调用解压函数进行解压，返回包含解压后数据的byte数组
        bufferedSink = sink.buffer()
        decompressBytes?.let { bufferedSink.write(it) } //将解压后数据写入文件（sink）中
        bufferedSink.close()

    }

    /**
     * 配置弹幕信息
     */

    private fun setDanmakuContextCongif() {

        // 设置弹幕的最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        //maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_LR] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_BOTTOM] = true

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F) //设置描边样式
            .setDuplicateMergingEnabled(false)
            .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
            .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
            .setMaximumLines(maxLinesPair) //设置最大显示行数
            .preventOverlapping(overlappingEnablePair) //设置防弹幕重叠，null为允许重叠

    }

    /**
     * 配置弹幕监听器
     */
    private fun setAsDanmakuCallback() {

        asDanmaku.setCallback(object : master.flame.danmaku.controller.DrawHandler.Callback {
            override fun prepared() {
                //弹幕准备好的时候回调，这里启动弹幕
                asDanmaku.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) {
                //定时器更新的时候回调
            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {
                //弹幕展示的时候回调
            }

            override fun drawingFinished() {
                //弹幕绘制完成时回调
            }

        })

    }


    /**
     * 创建解析器对象，解析输入流
     * @param stream
     * @return
     */
    private fun createParser(stream: InputStream?): BaseDanmakuParser {
        if (stream == null) {
            return object : BaseDanmakuParser() {
                override fun parse(): Danmakus {
                    return Danmakus()
                }
            }
        }
        val loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)
        try {
            loader.load(stream)
        } catch (e: IllegalDataException) {
            e.printStackTrace()
        }
        val parser: BaseDanmakuParser = BiliDanmukuParser()
        val dataSource = loader.dataSource
        parser.load(dataSource)
        return parser
    }


    //——————————————————————————————————————————————————————————————————————————

    override fun onResume() {
        super.onResume()
        if (asDanmaku.isPrepared && asDanmaku.isPaused) {
            //asDanmaku.show()
            // asJzvdStd.startButton.performClick()
        }
        //百度统计
        StatService.onResume(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        asDanmaku.release()
        JzvdStd.releaseAllVideos()
    }


    //解压deflate数据的函数
    fun decompress(data: ByteArray): ByteArray? {
        var output: ByteArray
        val decompresser = Inflater(true) //这个true是关键
        decompresser.reset()
        decompresser.setInput(data)
        val o = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(1024)
            while (!decompresser.finished()) {
                val i: Int = decompresser.inflate(buf)
                o.write(buf, 0, i)
            }
            output = o.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                o.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        decompresser.end()
        return output
    }


}