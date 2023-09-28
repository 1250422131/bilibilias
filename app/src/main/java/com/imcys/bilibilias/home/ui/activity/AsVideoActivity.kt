package com.imcys.bilibilias.home.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.view.AppAsJzvdStd
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.components.CenterRow
import com.imcys.bilibilias.common.base.components.VerticalTwoTerms
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.model.UserSpaceInformation
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoPageListData
import com.imcys.bilibilias.common.base.utils.VideoUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.common.base.view.JzbdStdInfo
import com.imcys.bilibilias.danmaku.BiliDanmukuParser
import com.imcys.bilibilias.home.ui.adapter.SubsectionAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.home.ui.viewmodel.AsVideoViewModel
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import com.imcys.bilibilias.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import master.flame.danmaku.controller.DrawHandler.Callback
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.loader.IllegalDataException
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import okio.use
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.zip.Inflater
import kotlin.collections.set

@AndroidEntryPoint
class AsVideoActivity : BaseActivity<com.imcys.bilibilias.databinding.ActivityAsVideoBinding>() {

    // 饺子播放器，方便全局调用
    private lateinit var asJzvdStd: AppAsJzvdStd

    // 烈焰弹幕使 弹幕解析器
    private lateinit var asDanmaku: IDanmakuView

    // 烈焰弹幕使，方便全局调用
    private lateinit var danmakuParser: BaseDanmakuParser

    private val danmakuContext = DanmakuContext.create()

    lateinit var userSpaceInformation: UserSpaceInformation

    private val viewModel by viewModels<AsVideoViewModel>()
    private var videoID by mutableStateOf("")
    private var epID by mutableStateOf("")

    // 视频临时数据，方便及时调用，此方案考虑废弃
    var bvid: String = ""
    var cid: Long = 0L
    private var epid: Long = 0L
    override fun getLayoutRes(): Int = R.layout.activity_as_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoID = intent.getStringExtra(BVID)!!
    }

    /**
     * 加载用户信息，为了确保会员视频及时通知用户
     */
    override fun initData() {
        lifecycleScope.launchIO {
            // userSpaceInformation = getUserData()
            // 显示用户卡片
            // showUserCard()
        }
        // initVideoData()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun initView() {
        binding.videoComposeView.setContent {
            BILIBILIASTheme {
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                val uiState by viewModel.videoUiState.collectAsStateWithLifecycle()
                LaunchedEffect(Unit) {
                    viewModel.getVideoData(videoID)
                }
                Scaffold(
                    Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            AndroidView(
                                factory = { context ->
                                    val appAsJzvdStd = AppAsJzvdStd(context)
                                    asJzvdStd = appAsJzvdStd
                                    asDanmaku = appAsJzvdStd.asDanmaku
                                    // 设置播放按钮功能
                                    when (asJzvdStd.state) {
                                        Jzvd.STATE_NORMAL, Jzvd.STATE_AUTO_COMPLETE -> {
                                            // 播放视频
                                            asJzvdStd.startVideo()
                                        }

                                        Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                                            // 恢复播放/暂停播放
                                            asJzvdStd.startButton.performClick()
                                        }
                                    }
                                    appAsJzvdStd.setUp(
                                        "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4",
                                        "饺子闭眼睛"
                                    )
                                    appAsJzvdStd.posterImageUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"
                                    setAsJzvdConfig(uiState.播放地址, uiState.title)
                                    appAsJzvdStd
                                },
                                Modifier.fillMaxSize()
                            ) { jzvd ->
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                        }) {
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
                ) { contentPadding ->
                    Column(
                        Modifier
                            .padding(contentPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 当前是展开还是收起

                        VideoActions()
                        Row(Modifier.fillMaxSize()) {
                            Text("选集")
                            Spacer(modifier = Modifier.weight(1f))
                            Text("已完结，全13话")
                        }
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(uiState.episodes, key = { _, item ->
                                item.bvid
                            }) { index, _ ->
                                Card(
                                    onClick = { /*TODO*/ },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        index.toString(),
                                        Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun VideoActions() {
        CenterRow(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            VerticalTwoTerms(
                top = {
                    Image(
                        painterResource(R.drawable.ic_as_video_like),
                        contentDescription = "点赞按钮",
                        Modifier.size(12.dp)
                    )
                },
                bottom = { Text("1百万", fontWeight = FontWeight.Thin) },
                Modifier
                    .clickable { }
                    .padding(16.dp)
                    .weight(1f)

            )
            VerticalTwoTerms(
                top = {
                    Image(
                        painterResource(R.drawable.ic_as_video_throw),
                        contentDescription = "投币按钮",
                        Modifier.size(12.dp)
                    )
                },
                bottom = { Text("1百万", fontWeight = FontWeight.Thin) },
                Modifier
                    .clickable { }
                    .padding(16.dp)
                    .weight(1f)

            )
            VerticalTwoTerms(
                top = {
                    Image(
                        painterResource(R.drawable.ic_as_video_collec),
                        contentDescription = "收藏按钮",
                        Modifier.size(12.dp)
                    )
                },
                bottom = { Text("1百万", fontWeight = FontWeight.Thin) },
                Modifier
                    .clickable { }
                    .padding(16.dp)
                    .weight(1f)

            )
            VerticalTwoTerms(
                top = {
                    Image(
                        painterResource(R.drawable.ic_as_video_fasong),
                        contentDescription = "分享按钮",
                        Modifier.size(12.dp)
                    )
                },
                bottom = { Text("1百万", fontWeight = FontWeight.Thin) },
                Modifier
                    .clickable { }
                    .padding(16.dp)
                    .weight(1f)

            )
        }
    }

    /**
     * 加载视频播放信息
     */
    private fun loadVideoPlay(type: String) {
        // 这里默认用目前flv最高免费画质1080P，注意：flv已经被B站弃用，目前只能使用360P和1080P，后面得考虑想办法做音频分离。
        when (type) {
            "video" -> {
                launchIO {
                    val videoPlayBean = viewModel.getMp4(bvid, cid, 64, 1, 1)
                    val dashVideoPlayBean =
                        viewModel.getDash(
                            bvid,
                            cid,
                            64,
                            16 or 64 or 128 or 256 or 512 or 1024 or 2048,
                            1
                        )
                    setAsJzvdConfig(videoPlayBean.durl[0].url, "")
                    dashVideoPlayBean.dash.video[0].also {
                        if (it.width < it.height) {
                            // 竖屏
                            // binding.asVideoAppbar.updateLayoutParams<ViewGroup.LayoutParams> {
                            //     height = windowManager.defaultDisplay.height / 4 * 3
                            // }
                        }

//                            binding.asVideoAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
//                                // 计算折叠程度（0为完全展开，1为完全折叠）
//
//                                if (asJzvdStd.state != Jzvd.STATE_NORMAL && asJzvdStd.state != Jzvd.STATE_AUTO_COMPLETE) {
//                                    // 根据当前滚动百分比计算内边距
//                                    val totalScrollRange = appBarLayout.totalScrollRange
//                                    val currentScrollPercentage = abs(verticalOffset) / totalScrollRange.toFloat()
//                                    val padding = (currentScrollPercentage * 100).toInt()
//                                    binding.asVideoAsJzvdStd.asJzvdstdVideo.setPadding(padding, 0, padding, padding)
//                                }
//
//                            }
                    }
                }
            }

            "bangumi" -> {
                setAsJzvdConfig(TODO("dddddddd"), "")
            }

            else -> "${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0&fourk=1"
        }
    }

    /**
     * 加载视频数据
     */
    private fun initVideoData() {
        // 加载视频播放信息
        loadVideoPlay("video")
        // loadVideoList() // 加载正常列表
        // }
        // 加载弹幕信息
        // loadDanmakuFlameMaster(it.cid.toString())

        // 检查三连情况
        // archiveHasLikeTriple()
    }

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
        viewModel.archiveFavoured(bvid)
    }

    /**
     * 检验投币情况
     */
    private suspend fun archiveCoins() {
        viewModel.archiveCoins(bvid)
    }

    /**
     * 检验是否点赞
     */
    private suspend fun archiveHasLike() {
        viewModel.archiveHasLike(bvid)
    }

    /**
     * 加载番剧视频列表信息
     *
     */
    private fun loadBangumiVideoList(epID: String) {
        // isMember(bangumiSeasonBean)

        // 获取真实的cid
        // bangumiSeasonBean.result.episodes.forEach { episode ->
        //     if (episode.bvid == bvid) {
        //         cid = episode.cid
        //     }
        // }
    }

    /**
     * 更新番剧信息
     * @param data EpisodesBean
     * @param position Int
     */
    private fun updateBangumiInformation(
        data: Bangumi.Result.Episode,
    ) {
        val userVipState = userSpaceInformation.vip.status
        if (data.badge == "会员" && userVipState != 1) {
            DialogUtils.dialog(
                this,
                "越界啦",
                "没大会员就要止步于此了哦，切换到不需要大会员的子集或者视频吧。",
                "我知道啦",
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
    private fun isMember(bangumiSeasonBean: Bangumi) {
        var memberType = false

        val userVipState = userSpaceInformation.vip.status
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
    private suspend fun getUserData(): UserSpaceInformation {
        return viewModel.getUserData()
    }

    /**
     * 加载视频列表信息
     *
     */
    private fun loadVideoList() {
        launchIO {
            val videoPlayListData = listOf<VideoPageListData>()
            KtHttpUtils.addHeader(COOKIE, asUser.cookie)
                .asyncGet<VideoPageListData>(BilibiliApi.videoPageListPath + "?bvid=" + bvid)

            launchUI {
                binding.apply {
                    // asVideoSubsectionRv.adapter =
                    // 将子集切换后的逻辑交给activity完成
                    SubsectionAdapter(videoPlayListData.toMutableList()) { data, _ ->
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

                    // asVideoSubsectionRv.layoutManager =
                }
            }
        }
    }

    /**
     * 加载弹幕信息
     */
    private fun loadDanmakuFlameMaster(cid: String = "") {
        launchIO {
            val bytes = viewModel.loadDanmakuFlameMaster(cid)
            saveDanmaku(bytes)
            // 初始化弹幕配置
            initDanmaku()
        }
    }

    /**
     * 初始化弹幕
     */
    private fun initDanmaku() {
        // 我们得先从临时文件拉取弹幕xml
        val streamReader = File(getExternalFilesDir("temp"), "tempDm.xml").inputStream()

        // 弹幕解析器
        danmakuParser = createParser(streamReader)

        // 设置弹幕配置
        setDanmakuContextCongif()

        // 设置弹幕监听器
        setAsDanmakuCallback()
    }

    /**
     * 加载用户卡片
     * @param mid Long
     */
    private fun loadUserCardData() {
        launchIO {
            viewModel.loadUserCardData()
        }
    }

    /**
     * 显示用户卡片
     */
    private fun showUserCard() {
        // 判断是否会员，会员情况下展示会员主题色，反之黑色
        val nameColor = if (userSpaceInformation.vip.nicknameColor.isNotBlank()) {
            Color.parseColor(userSpaceInformation.vip.nicknameColor)
        } else {
            // 低版本兼容
            Color.parseColor("#FB7299")
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
        asDanmaku.pause()
        if (asJzvdStd.state == Jzvd.STATE_PLAYING) { // 暂停视频
            asJzvdStd.startButton.performClick()
            changeFaButtonToPlay()
        }
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
            intent.putExtra("bvId", VideoUtils.toBvidOffline(aid))
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
        // binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_play)
    }

    private fun changeFaButtonToPause() {
        // binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_pause)
    }

    private fun changeFaButtonToRedo() {
        // binding.asVideoFaButton.isVisible = true
        // binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_redo)
    }

// ——————————————————————————————————————————————————————————————————————————

// ——————————————————————————————————————————————————————————————————————————
// 弹幕抽离
    /**
     * 储存弹幕内容
     */
    private fun saveDanmaku(bytes: ByteArray) {
        File(getExternalFilesDir("temp"), "tempDm.xml").run {
            if (!exists()) createNewFile()
            bufferedWriter().use {
                writeBytes(bytes)
            }
        }
    }

    /**
     * 配置弹幕信息
     */

    private fun setDanmakuContextCongif() {
        // 设置弹幕的最大显示行数
        val maxLinesPair = mapOf<Int, Int>()
        // maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        val overlappingEnablePair = mapOf(
            BaseDanmaku.TYPE_SCROLL_LR to true,
            BaseDanmaku.TYPE_FIX_BOTTOM to true
        )

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
        asDanmaku.setCallback(object : Callback {
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
            Timber.tag("解析弹幕文件异常").d(e)
        }
        val parser: BaseDanmakuParser = BiliDanmukuParser()
        val dataSource = loader.dataSource
        parser.load(dataSource)
        return parser
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
