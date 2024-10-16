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
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.baidu.mobstat.StatService
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.view.AppAsJzvdStd
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.common.base.view.JzbdStdInfo
import com.imcys.bilibilias.common.network.base.ResBean
import com.imcys.bilibilias.danmaku.BiliDanmukuParser
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.adapter.BangumiSubsectionAdapter
import com.imcys.bilibilias.home.ui.adapter.SubsectionAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.home.ui.viewmodel.AsVideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
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
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.Inflater
import javax.inject.Inject

@AndroidEntryPoint
class AsVideoActivity : BaseActivity() {

    private val TAG = this.javaClass.name

    // 视频基本数据类，方便全局调用
    private lateinit var videoDataBean: VideoBaseBean

    lateinit var binding: ActivityAsVideoBinding

    // 饺子播放器，方便全局调用
    private lateinit var asJzvdStd: AppAsJzvdStd

    // 烈焰弹幕使 弹幕解析器
    private lateinit var asDanmaku: IDanmakuView

    // 烈焰弹幕使，方便全局调用
    private lateinit var danmakuParser: BaseDanmakuParser
    private val danmakuContext = DanmakuContext.create()

    lateinit var userBaseBean: UserBaseBean

    private val asVideoViewModel: AsVideoViewModel by viewModels()

    @Inject
    lateinit var networkService: NetworkService

    // 视频临时数据，方便及时调用，此方案考虑废弃
    var bvid: String = ""
    var avid: Long = 0L
    var cid: Long = 0L
    var epid: Long = 0L

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_as_video)

        // 加载用户信息&视频信息
        loadUserData()
        // 加载控件
        initView()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(newBase)
//
//    }
    /**
     * 加载用户信息，为了确保会员视频及时通知用户
     */
    private fun loadUserData() {
        launchUI {
            userBaseBean = withContext(Dispatchers.IO) { getUserData() }
            // 加载视频首要信息
            initVideoData()
        }
    }

    private fun initView() {
        binding.apply {
            // 绑定播放器，弹幕控制器
            asJzvdStd = asVideoAsJzvdStd
            asDanmaku = asVideoAsJzvdStd.asDanmaku

            // 设置播放按钮功能
            asVideoFaButton.setOnClickListener {
                when (asJzvdStd.state) {
                    Jzvd.STATE_NORMAL, Jzvd.STATE_AUTO_COMPLETE -> {
                        // 播放视频
                        asVideoFaButton.visibility = View.GONE
                        asJzvdStd.startVideo()
                    }

                    Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                        // 恢复播放/暂停播放
                        asJzvdStd.startButton.performClick()
                    }
                }
            }

            // 设置点击事件->这里将点击事件都放这个类了
            asVideoViewModel = this@AsVideoActivity.asVideoViewModel
        }
    }

    /**
     * 加载视频播放信息
     */
    private fun loadVideoPlay(type: String) {
        // 这里默认用目前flv最高免费画质1080P，注意：flv已经被B站弃用，目前只能使用360P和1080P，后面得考虑想办法做音频分离。

        when (type) {
            "video" -> {
                launchUI {
                    // 获取播放信息
                    val videoPlayBean = networkService.getVideoPlayInfo(bvid, cid)
                    // 设置布局视频播放数据
                    binding.videoPlayBean = videoPlayBean
                    // 有部分视频不存在flv接口下的mp4，无法提供播放服务，需要及时通知。
                    if (videoPlayBean.code != 0) {
                        // 弹出通知弹窗
                        AsDialog.init(this@AsVideoActivity).build {
                            title =
                                getString(R.string.app_asvideoactivity_loadvideoplay_asdialog_title)
                            config = {
                                content =
                                    getString(R.string.app_asvideoactivity_loadvideoplay_asdialog_content)
                                positiveButtonText =
                                    getString(R.string.app_asvideoactivity_loadvideoplay_asdialog_button_text)
                                positiveButton = {
                                    it.cancel()
                                }
                            }
                        }.show()
                    } else {
                        val dashVideoPlayBean = networkService.n10(bvid, cid)
                        if (dashVideoPlayBean.code != 0) {
                            setAsJzvdConfig(videoPlayBean.data.durl[0].url, "")
                        }

                        if (dashVideoPlayBean.data.dash.video.isNotEmpty()) {
                            // 得有video才行
                            dashVideoPlayBean.data.dash.video[0].also {
                                if (it.width < it.height) {
                                    // 竖屏
                                    binding.asVideoAppbar.updateLayoutParams<ViewGroup.LayoutParams> {
                                        height = windowManager.defaultDisplay.height / 4 * 3
                                    }
                                }
                            }
                        }

                        // 真正调用饺子播放器设置视频数据
                        setAsJzvdConfig(videoPlayBean.data.durl[0].url, "")
                    }

                    binding.asVideoCd.visibility = View.VISIBLE
                    binding.asVideoBangumiCd.visibility = View.GONE
                }
            }

            "bangumi" -> {
                launchIO {
                    val bangumiPlayBean = networkService.n16(epid)

                    launchUI {
                        // 设置布局视频播放数据
                        binding.bangumiPlayBean = bangumiPlayBean
                        // 真正调用饺子播放器设置视频数据
                        setAsJzvdConfig(bangumiPlayBean.result.durl[0].url, "")
                    }
                }
            }

            else -> "${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0&fourk=1"
        }
    }

    /**
     * 加载视频数据
     */
    private fun initVideoData() {
        // 这里必须通过外界获取数据
        val intent = intent

        val bvId = intent.getStringExtra("bvId")

        launchUI {
            var videoBaseBean = networkService.getVideoBaseInfoByBvid(bvId.toString())

            if (videoBaseBean.code != 0) {
                videoBaseBean = networkService.getVideoBaseInfoByAid(
                    NewVideoNumConversionUtils.bv2av(bvId ?: "").toString()
                )
            }

            // 设置数据
            videoDataBean = videoBaseBean
            // 这里需要显示视频数据
            showVideoData()
            // 设置基本数据，注意这里必须优先，因为我们在后面会复用这些数据
            setBaseData(videoBaseBean)
            // 加载用户卡片
            loadUserCardData(videoBaseBean.data.owner.mid)
            // 加载弹幕信息
            loadDanmakuFlameMaster()
            // 加载视频列表信息，这里判断下是不是番剧，由于正常来说，普通视频是没有redirect_url的
            videoBaseBean.data.redirect_url.apply {
                if (this != "") {
                    // 通过正则表达式检查该视频是不是番剧
                    val epRegex = Regex("""(?<=ep)(\d*)""")
                    if (epRegex.containsMatchIn(this)) {
                        // 加载番剧视频列表
                        epid = epRegex.find(this)?.value?.toLong()!!
                        loadBangumiVideoList()
                    }
                } else {
                    // 加载视频播放信息
                    loadVideoPlay("video")
                    loadVideoList() // 加载正常列表
                }
            }
            // 检查三连情况
            archiveHasLikeTriple()
        }
    }

    @Inject
    lateinit var http: HttpClient

    /**
     * 检查三连情况
     */
    private fun archiveHasLikeTriple() {
        launchIO {
            archiveHasLike()
            archiveCoins()
            archiveFavoured()
        }
    }

    /**
     * 收藏检验
     */
    private suspend fun archiveFavoured() {
        val bean = http.get("${BilibiliApi.archiveFavoured}?aid=$bvid")
            .body<ResBean<ArchiveFavouredBean>>()
        binding.archiveFavouredBean = bean.data
    }

    /**
     * 检验投币情况
     */
    private suspend fun archiveCoins() {
        val bean = http.get("${BilibiliApi.archiveHasLikePath}?bvid=$bvid")
            .body<ArchiveHasLikeBean>()
        binding.archiveHasLikeBean = bean
    }

    /**
     * 检验是否点赞
     */
    private suspend fun archiveHasLike() {
        val bean =
            http.get("${BilibiliApi.archiveCoinsPath}?bvid=$bvid").body<ResBean<ArchiveCoinsBean>>()
        binding.archiveCoinsBean = bean.data
    }

    /**
     * 加载番剧视频列表信息
     *
     */
    private fun loadBangumiVideoList() {
        launchIO {
            val bangumiSeasonBean = networkService.getBangumiSeasonBeanByEpid(epid)
            launchUI { isMember(bangumiSeasonBean) }

            // 获取真实的cid
            bangumiSeasonBean.result.episodes.forEach { episode ->
                if (episode.bvid == bvid) {
                    cid = episode.cid
                }
            }

            launchUI {
                binding.apply {
                    // 如果就只有一个子集，就不要显示子集列表了
                    if (bangumiSeasonBean.result.episodes.size == 1) {
                        asVideoSubsectionRv.visibility =
                            View.GONE
                    }

                    // 到这里就毋庸置疑的说，是番剧，要单独加载番剧缓存。
                    asVideoBangumiCd.visibility = View.VISIBLE
                    asVideoCd.visibility = View.GONE
                    this.bangumiSeasonBean = bangumiSeasonBean

                    asVideoSubsectionRv.adapter =
                        BangumiSubsectionAdapter(
                            bangumiSeasonBean.result.episodes.toMutableList(),
                            cid,
                        ) { data, _ ->
                            // 这里需要判断子集选择，我们得确定这并不是一个会员视频。或者说用户有会员可以去播放
                            updateBangumiInformation(data)
                        }

                    asVideoSubsectionRv.layoutManager =
                        LinearLayoutManager(
                            this@AsVideoActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false,
                        )
                }
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
        if (data.badge == getString(R.string.app_asvideoactivity_updatebangumiinformation_badge) && userVipState != 1) {
            DialogUtils.dialog(
                this,
                getString(R.string.app_asvideoactivity_updatebangumiinformation_dialog_title),
                getString(R.string.app_asvideoactivity_updatebangumiinformation_dialog_message),
                getString(R.string.app_asvideoactivity_updatebangumiinformation_dialog_button_text),
                positiveButtonClickListener = {
                },
            ).show()
        } else {
            // 更新CID刷新播放页面
            cid = data.cid
            bvid = data.bvid
            epid = data.id

            // 更新海报->确保可以下载每一个子集的海报
            asJzvdStd.updatePoster(data.cover)
            // 暂停播放
            changeFaButtonToPlay()
            // 清空弹幕->因为要有新弹幕进来
            asDanmaku.release()
            // 刷新播放器
            loadVideoPlay("bangumi")
            // 更新弹幕
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
            if (it.cid == cid && it.badge == getString(R.string.app_asvideoactivity_ismember_badge) && userVipState != 1) memberType = true
        }
        if (memberType) {
            DialogUtils.dialog(
                this,
                getString(R.string.app_asvideoactivity_ismember_dialog_title),
                getString(R.string.app_asvideoactivity_ismember_dialog_message),
                getString(R.string.app_asvideoactivity_ismember_dialog_button_text),
                positiveButtonClickListener = {
                },
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
        return networkService.n11(asUser.mid)
    }

    /**
     * 加载视频列表信息
     *
     */
    private fun loadVideoList() {
        launchIO {
            val videoPlayListData = networkService.getVideoPageListData(bvid)

            launchUI {
                binding.apply {
                    if (videoPlayListData.data.size == 1) asVideoSubsectionRv.visibility = View.GONE

                    binding.videoPageListData = videoPlayListData
                    asVideoSubsectionRv.adapter =
                        // 将子集切换后的逻辑交给activity完成
                        SubsectionAdapter(videoPlayListData.data.toMutableList()) { data, _ ->
                            // 更新CID刷新播放页面
                            cid = data.cid
                            // 暂停播放
                            changeFaButtonToPlay()
                            // 刷新播放器
                            loadVideoPlay("video")
                            // 清空弹幕
                            asDanmaku.release()
                            // 更新弹幕
                            loadDanmakuFlameMaster()
                        }

                    asVideoSubsectionRv.layoutManager =
                        LinearLayoutManager(
                            this@AsVideoActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false,
                        )
                }
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
        launchUI {
            runCatching {
                // 储存弹幕
                saveDanmaku(networkService.getDanmuBytes(cid))
            }
            // 初始化弹幕配置
            initDanmaku()
        }
    }

    /**
     * 初始化弹幕
     */
    private fun initDanmaku() {
        // 我们得先从临时文件拉取弹幕xml
        val input: InputStream =
            FileInputStream(getExternalFilesDir("temp").toString() + "/tempDm.xml")

        // 解析弹幕
        danmakuParser = createParser(input)

        // 设置弹幕配置
        setDanmakuContextCongif()

        // 设置弹幕监听器
        setAsDanmakuCallback()
    }

    /**
     * 加载用户卡片
     * @param mid Long
     */
    private fun loadUserCardData(mid: Long) {
        launchUI {
            val userCardBean = networkService.getUserCardData(mid)
            // 显示用户卡片
            showUserCard()
            // 将数据交给viewModel
            binding.userCardBean = userCardBean
        }
    }

    /**
     * 显示用户卡片
     */
    private fun showUserCard() {
        binding.apply {
            asVideoUserCardLy.visibility = View.VISIBLE

            // 判断是否会员，会员情况下展示会员主题色，反之黑色
            val nameColor = if (userBaseBean.data.vip.nickname_color != "") {
                Color.parseColor(userBaseBean.data.vip.nickname_color)
            } else {
                // 低版本兼容
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getColor(R.color.black)
                } else {
                    Color.BLACK
                }
            }

            // 设置最终获取颜色
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // 释放播放器
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
        if (asJzvdStd.state == Jzvd.STATE_PLAYING) { // 暂停视频
            asJzvdStd.startButton.performClick()
            changeFaButtonToPlay()
        }
        // 百度统计
        StatService.onPause(this)
    }

    companion object {

        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }

        @Deprecated("B站已经在弱化aid的使用，我们不确定这是否会被弃用，因此这个方法将无法确定时效性")
        fun actionStart(context: Context, aid: Long) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", NewVideoNumConversionUtils.av2bv(aid))
            context.startActivity(intent)
        }
    }

    /**
     * 设置饺子播放器参数配置
     * @param url String
     * @param title String
     */
    private fun setAsJzvdConfig(url: String, title: String) {
        // val map = LinkedHashMap<Any, Any>()
        // map["760P"] = url
        val jzDataSource = JZDataSource(url, title)

        jzDataSource.headerMap[COOKIE] = asUser.cookie
        jzDataSource.headerMap[REFERER] = "$BILIBILI_URL/video/$bvid"
        jzDataSource.headerMap[USER_AGENT] = BROWSER_USER_AGENT

        asJzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)

        asJzvdStd.setPlayStateListener(object : JzbdStdInfo {

            override fun statePlaying(state: Int) {
                changeFaButtonToPause()

                if (state == Jzvd.STATE_PAUSE) {
                    // 判断暂停的事时间是不是不等同于现在的播放时间
                    // 如果不是相当于重新播放
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
                // 暂停弹幕
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

    // ——————————————————————————————————————————————————————————————————————————
    // 悬浮按钮状态更新
    private fun changeFaButtonToPlay() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_play)
    }

    private fun changeFaButtonToPause() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_pause)
    }

    private fun changeFaButtonToRedo() {
        binding.asVideoFaButton.isVisible = true
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_redo)
    }

    // ——————————————————————————————————————————————————————————————————————————

    // ——————————————————————————————————————————————————————————————————————————
    // 弹幕抽离
    /**
     * 储存弹幕内容
     */
    private fun saveDanmaku(bytes: ByteArray) {
        val bufferedSink: BufferedSink?
        val dest = File(getExternalFilesDir("temp").toString(), "tempDm.xml")
        if (!dest.exists()) dest.createNewFile()
        val sink = dest.sink() // 打开目标文件路径的sink
        val decompressBytes =
            decompress(bytes) // 调用解压函数进行解压，返回包含解压后数据的byte数组
        bufferedSink = sink.buffer()
        decompressBytes.let { bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
        bufferedSink.close()
    }

    /**
     * 配置弹幕信息
     */

    private fun setDanmakuContextCongif() {
        // 设置弹幕的最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        // maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_LR] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_BOTTOM] = true

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F) // 设置描边样式
            .setDuplicateMergingEnabled(false)
            .setScrollSpeedFactor(1.2f) // 是否启用合并重复弹幕
            .setScaleTextSize(1.2f) // 设置弹幕滚动速度系数,只对滚动弹幕有效
            .setMaximumLines(maxLinesPair) // 设置最大显示行数
            .preventOverlapping(overlappingEnablePair) // 设置防弹幕重叠，null为允许重叠
    }

    /**
     * 配置弹幕监听器
     */
    private fun setAsDanmakuCallback() {
        asDanmaku.setCallback(object : master.flame.danmaku.controller.DrawHandler.Callback {
            override fun prepared() {
                // 弹幕准备好的时候回调，这里启动弹幕
                asDanmaku.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) {
                // 定时器更新的时候回调
            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {
                // 弹幕展示的时候回调
            }

            override fun drawingFinished() {
                // 弹幕绘制完成时回调
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

    // ——————————————————————————————————————————————————————————————————————————

    override fun onResume() {
        super.onResume()
        if (asDanmaku.isPrepared && asDanmaku.isPaused) {
            // asDanmaku.show()
            // asJzvdStd.startButton.performClick()
        }
        // 百度统计
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
        val decompresser = Inflater(true) // 这个true是关键
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
