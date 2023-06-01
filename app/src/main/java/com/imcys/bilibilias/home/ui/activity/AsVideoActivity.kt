package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.baidu.mobstat.StatService
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.TokenUtils
import com.imcys.bilibilias.base.view.AppAsJzvdStd
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.VideoNumConversion
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.common.base.view.JzbdStdInfo
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
    private lateinit var asJzvdStd: AppAsJzvdStd

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


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_as_video)

        //加载用户信息&视频信息
        loadUserData()
        //加载控件
        initView()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(newBase)
//
//    }
    /**
     * 加载用户信息，为了确保会员视频及时通知用户
     */
    private fun loadUserData() {
        lifecycleScope.launch {

            userBaseBean = withContext(lifecycleScope.coroutineContext) { getUserData() }
            //加载视频首要信息
            initVideoData()
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
                        asVideoFaButton.visibility = View.GONE
                        asJzvdStd.startVideo()
                    }

                    Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                        //恢复播放/暂停播放
                        asJzvdStd.startButton.performClick()
                    }
                }
            }

            //设置点击事件->这里将点击事件都放这个类了
            asVideoViewModel = ViewModelProvider(
                this@AsVideoActivity
            )[AsVideoViewModel::class.java]


        }
    }


    /**
     * 加载视频播放信息
     */
    private fun loadVideoPlay(type: String) {

        //这里默认用目前flv最高免费画质1080P，注意：flv已经被B站弃用，目前只能使用360P和1080P，后面得考虑想办法做音频分离。

        when (type) {
            "video" -> {
                lifecycleScope.launchWhenCreated {
                    //获取播放信息
                    val videoPlayBean = KtHttpUtils.addHeader("cookie", asUser.cookie)
                        .addHeader("referer", "https://www.bilibili.com")
                        .asyncGet<VideoPlayBean>("${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0&fourk=1")
                    //设置布局视频播放数据
                    binding.videoPlayBean = videoPlayBean

                    //有部分视频不存在flv接口下的mp4，无法提供播放服务，需要及时通知。
                    if (videoPlayBean.code != 0) {
                        //弹出通知弹窗
                        AsDialog.init(this@AsVideoActivity).build {
                            title = "视频文件特殊"
                            config = {
                                content = "该视频无FLV格式，故无法播放，请选择Dash模式缓存。"
                                positiveButtonText = "知道啦"
                                positiveButton = {
                                    it.cancel()
                                }
                            }
                        }.show()

                    } else {

                        val dashVideoPlayBean = KtHttpUtils.addHeader(
                            "cookie",
                            BaseApplication.dataKv.decodeString("cookies", "")!!
                        )
                            .addHeader("referer", "https://www.bilibili.com")
                            .asyncGet<DashVideoPlayBean>("${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=4048&fourk=1")

                        if (dashVideoPlayBean.code != 0) setAsJzvdConfig(
                            videoPlayBean.data.durl[0].url,
                            ""
                        )

                        dashVideoPlayBean.data.dash.video[0].also {

                            if (it.width < it.height) {
                                //竖屏
                                binding.asVideoAppbar.updateLayoutParams<ViewGroup.LayoutParams> {
                                    height = windowManager.defaultDisplay.height / 4 * 3
                                }
                            }

                        }

                        //真正调用饺子播放器设置视频数据
                        setAsJzvdConfig(videoPlayBean.data.durl[0].url, "")
                    }
                    //这里要展示视频信息卡片
                    binding.asVideoCd.visibility = View.VISIBLE
                    binding.asVideoBangumiCd.visibility = View.GONE
                }
            }

            "bangumi" -> {
                lifecycleScope.launch {
                    val bangumiPlayBean = KtHttpUtils
                        .addHeader("cookie", asUser.cookie)
                        .addHeader("referer", "https://www.bilibili.com")
                        .asyncGet<BangumiPlayBean>("${BaseApplication.roamApi}pgc/player/web/playurl?ep_id=$epid&qn=64&fnval=0&fourk=1")
                    //设置布局视频播放数据
                    binding.bangumiPlayBean = bangumiPlayBean
                    //真正调用饺子播放器设置视频数据
                    setAsJzvdConfig(bangumiPlayBean.result.durl[0].url, "")

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

        lifecycleScope.launch {

            val videoBaseBean = KtHttpUtils.run {
                addHeader("cookie", asUser.cookie)
                asyncGet<VideoBaseBean>(BilibiliApi.getVideoDataPath + "?bvid=$bvId")
            }

            //设置数据
            videoDataBean = videoBaseBean
            //这里需要显示视频数据
            showVideoData()
            //TODO 设置基本数据，注意这里必须优先，因为我们在后面会复用这些数据
            setBaseData(videoBaseBean)
            //加载用户卡片
            loadUserCardData(videoBaseBean.data.owner.mid)
            //加载弹幕信息
            loadDanmakuFlameMaster()
            //加载视频列表信息，这里判断下是不是番剧，由于正常来说，普通视频是没有redirect_url的
            videoBaseBean.data.redirect_url?.apply {

                //通过正则表达式检查该视频是不是番剧
                val epRegex = Regex("""(?<=ep)(\d*)""")
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
        lifecycleScope.launch {
            archiveHasLike()
            archiveCoins()
            archiveFavoured()
        }
    }

    /**
     * 收藏检验
     */
    private suspend fun archiveFavoured() {
        val archiveFavouredBean = KtHttpUtils.addHeader("cookie", asUser.cookie)
            .asyncGet<ArchiveFavouredBean>("${BilibiliApi.archiveFavoured}?aid=${bvid}")
        binding.archiveFavouredBean = archiveFavouredBean
    }

    /**
     * 检验投币情况
     */
    private suspend fun archiveCoins() {
        val archiveHasLikeBean = KtHttpUtils.addHeader("cookie", asUser.cookie)
            .asyncGet<ArchiveHasLikeBean>("${BilibiliApi.archiveHasLikePath}?bvid=${bvid}")
        binding.archiveHasLikeBean = archiveHasLikeBean
    }

    /**
     * 检验是否点赞
     */
    private suspend fun archiveHasLike() {
        val archiveCoinsBean = KtHttpUtils.addHeader("cookie", asUser.cookie)
            .asyncGet<ArchiveCoinsBean>("${BilibiliApi.archiveCoinsPath}?bvid=${bvid}")
        binding.archiveCoinsBean = archiveCoinsBean

    }

    /**
     * 加载番剧视频列表信息
     *
     */
    private fun loadBangumiVideoList() {

        lifecycleScope.launch {

            val bangumiSeasonBean = KtHttpUtils.run {
                //弃用漫游服务
                addHeader("cookie", asUser.cookie)
                asyncGet<BangumiSeasonBean>(BaseApplication.roamApi + "pgc/view/web/season?ep_id=" + epid)
            }
            isMember(bangumiSeasonBean)


            binding.apply {
                //如果就只有一个子集，就不要显示子集列表了
                if (bangumiSeasonBean.result.episodes.size == 1) asVideoSubsectionRv.visibility =
                    View.GONE

                //到这里就毋庸置疑的说，是番剧，要单独加载番剧缓存。
                asVideoBangumiCd.visibility = View.VISIBLE
                asVideoCd.visibility = View.GONE
                this.bangumiSeasonBean = bangumiSeasonBean

                asVideoSubsectionRv.adapter =
                    BangumiSubsectionAdapter(
                        bangumiSeasonBean.result.episodes.toMutableList(),
                        cid
                    ) { data, _ ->
                        //这里需要判断子集选择，我们得确定这并不是一个会员视频。或者说用户有会员可以去播放
                        updateBangumiInformation(data)
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
            epid = data.id

            //更新海报->确保可以下载每一个子集的海报
            asJzvdStd.updatePoster(data.cover)
            //暂停播放
            changeFaButtonToPlay()
            //清空弹幕->因为要有新弹幕进来
            asDanmaku.release()
            //刷新播放器
            loadVideoPlay("bangumi")
            //更新弹幕
            loadDanmakuFlameMaster()
        }

    }

    /**
     * 会员检测，确保用户有会员，或者无会员的提示
     * @param bangumiSeasonBean BangumiSeasonBean
     */
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

        val params = mutableMapOf<String?, String?>()
        params["mid"] = asUser.mid.toString()
        val paramsStr = TokenUtils.getParamStr(params)

        return KtHttpUtils.addHeader("cookie", asUser.cookie)
            .asyncGet("${BilibiliApi.userBaseDataPath}?$paramsStr")
    }


    /**
     * 加载视频列表信息
     *
     */
    private fun loadVideoList() {
        lifecycleScope.launch {
            val videoPlayListData = KtHttpUtils.addHeader("cookie", asUser.cookie)
                .asyncGet<VideoPageListData>(BilibiliApi.videoPageListPath + "?bvid=" + bvid)
            binding.apply {

                if (videoPlayListData.data.size == 1) asVideoSubsectionRv.visibility = View.GONE

                binding.videoPageListData = videoPlayListData
                asVideoSubsectionRv.adapter =
                        //将子集切换后的逻辑交给activity完成
                    SubsectionAdapter(videoPlayListData.data.toMutableList()) { data, _ ->
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
     * 加载弹幕信息(目前只能这样写)
     */
    private fun loadDanmakuFlameMaster() {

        HttpUtils.addHeader("cookie", asUser.cookie)
            .get("${BilibiliApi.videoDanMuPath}?oid=$cid",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {

                        BaseApplication.handler.post {
                            //储存弹幕
                            saveDanmaku(response.body!!.bytes())
                            //初始化弹幕配置
                            initDanmaku()
                        }
                    }

                })
    }


    /**
     * 初始化弹幕
     */
    private fun initDanmaku() {
        //我们得先从临时文件拉取弹幕xml
        val input: InputStream =
            FileInputStream(getExternalFilesDir("temp").toString() + "/tempDm.xml")

        //解析弹幕
        danmakuParser = createParser(input)

        //设置弹幕配置
        setDanmakuContextCongif()

        //设置弹幕监听器
        setAsDanmakuCallback()

    }


    /**
     * 加载用户卡片
     * @param mid Long
     */
    private fun loadUserCardData(mid: Long) {
        lifecycleScope.launch {
            val userCardBean = KtHttpUtils
                .addHeader("cookie", asUser.cookie)
                .asyncGet<UserCardBean>(BilibiliApi.getUserCardPath + "?mid=$mid")

            //显示用户卡片
            showUserCard()
            //将数据交给viewModel
            binding.userCardBean = userCardBean

        }


    }

    /**
     * 显示用户卡片
     */
    private fun showUserCard() {
        binding.apply {
            asVideoUserCardLy.visibility = View.VISIBLE

            //判断是否会员，会员情况下展示会员主题色，反之黑色
            val nameColor = if (userBaseBean.data?.vip?.nickname_color != "") {
                Color.parseColor(userBaseBean.data?.vip?.nickname_color!!)
            } else {
                //低版本兼容
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getColor(R.color.black)
                } else {
                    Color.BLACK
                }
            }

            //设置最终获取颜色
            asVideoUserName.setTextColor(nameColor)

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
        //释放播放器
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

        jzDataSource.headerMap["Cookie"] = asUser.cookie
        jzDataSource.headerMap["Referer"] = "https://www.bilibili.com/video/$bvid";
        jzDataSource.headerMap["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0";

        asJzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)

        asJzvdStd.setPlayStateListener(object : JzbdStdInfo {

            override fun statePlaying(state: Int) {

                changeFaButtonToPause()

                if (state == Jzvd.STATE_PAUSE) {
                    //判断暂停的事时间是不是不等同于现在的播放时间
                    //如果不是相当于重新播放
                    if (asJzvdStd.stopTime != asJzvdStd.currentPositionWhenPlaying) {
                        asDanmaku.start(asJzvdStd.currentPositionWhenPlaying)
                    } else {
                        asDanmaku.resume()
                    }
                } else {
                    asDanmaku.prepare(danmakuParser, danmakuContext)
                    asDanmaku.enableDanmakuDrawingCache(true)
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

            /**
             * 这里是个滚动手势监听，触发代表拖动了进度条。
             * @param state Int
             */
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


    /**
     * 解压deflate数据的函数（弹幕需要）
     * @param data ByteArray
     * @return ByteArray?
     */
    fun decompress(data: ByteArray): ByteArray {
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