package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import coil.load
import com.baidu.mobstat.StatService
import com.hjq.toast.Toaster
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
import com.imcys.bilibilias.core.model.user.Card
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewTriple
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.activity.video.BiliDanmukuUtil
import com.imcys.bilibilias.home.ui.adapter.BangumiSubsectionAdapter
import com.imcys.bilibilias.home.ui.adapter.SubsectionAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.home.ui.viewmodel.AsVideoViewModel
import com.imcys.bilibilias.home.ui.viewmodel.Event
import com.imcys.bilibilias.home.ui.viewmodel.player.ViewUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.Inflater
import javax.inject.Inject
import kotlin.collections.set

@AndroidEntryPoint
class AsVideoActivity : BaseActivity<ActivityAsVideoBinding>() {
    override val layoutId: Int = R.layout.activity_as_video

    // 视频基本数据类，方便全局调用
    private lateinit var videoDataBean: VideoBaseBean

    // 饺子播放器，方便全局调用
    private lateinit var asJzvdStd: AppAsJzvdStd

    // 烈焰弹幕使 弹幕解析器
    private lateinit var asDanmaku: IDanmakuView

    lateinit var userBaseBean: UserBaseBean

    private val viewModel: AsVideoViewModel by viewModels()

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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun initView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._effect.consumeAsFlow().collect {
                    when (it) {
                        is Event.ShowToast -> Toaster.show(it.text)
                        is Event.ToolBarReportChange -> renderViewTriple(it.viewTriple)
                        is Event.ShowFavouredDialog -> Unit
                    }
                }
            }
        }

        binding.asVideoCollectionLy.setOnClickListener {
        }
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
            asVideoViewModel = this@AsVideoActivity.viewModel
        }
    }

    override fun initData() {
        lifecycleScope.launch {
            viewModel.asVideoUiState.flowWithLifecycle(lifecycle).collect {
                render(it)
            }
        }
    }

    private fun render(state: ViewUiState) {
        when (state) {
            ViewUiState.Loading -> Unit
            is ViewUiState.Success -> {
                renderTitleWithDesc(state.viewDetail)
                renderUpInfoContainer(state.userCard)
                renderToolBarReport(state.viewDetail, state.viewTriple)
            }

        }
    }

    private fun renderUpInfoContainer(userCard: Card) {
        val card = userCard.card
        binding.ivOwnerFace.load(card.face)
        binding.asVideoUserName.text = card.name
        binding.tvOwnerData.text =
            "%s粉丝\t%s投稿".format(card.fans, userCard.archiveCount)
    }

    private fun renderTitleWithDesc(viewDetail: ViewDetail) {
        val title = viewDetail.title
        binding.tvViewTitle.text = title
        binding.tvViewTitle.setOnClickListener {
            textCopyThenPost(title)
        }
        binding.tvViewDesc.text = viewDetail.descV2.first().rawText
    }

    private fun textCopyThenPost(textCopied: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", textCopied))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toaster.show(R.string.Copied)
        }
    }

    private fun renderToolBarReport(viewDetail: ViewDetail, viewTriple: ViewTriple) {
        binding.asVideoLikeLy.setOnClickListener {
            viewModel.likeVideo(viewTriple.hasLike, viewDetail.bvid)
        }
        binding.asVideoThrowLy.setOnClickListener {
            viewModel.videoCoinAdd(viewDetail.bvid)
        }
        binding.asVideoCollectionLy.setOnClickListener {
//         viewModel.loadCollectionView()
            viewModel.getUserFavorites()

        }

        val stat = viewDetail.stat
        binding.tvViewStatLike.text = stat.like.toString()
        binding.tvViewStatCoins.text = stat.coin.toString()
        binding.tvViewStatFavoured.text = stat.favorite.toString()
        binding.tvViewStatShared.text = stat.share.toString()
        renderViewTriple(viewTriple)
    }

    private fun renderViewTriple(viewTriple: ViewTriple) {
        binding.asVideoLikeBt.isSelected = viewTriple.hasLike
        binding.asVideoThrowBt.isSelected = viewTriple.hasLike
        binding.asVideoFaButton.isSelected = viewTriple.hasLike
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
                    val videoPlayBean = networkService.n9(bvid, cid)
                    // 设置布局视频播放数据
                    binding.videoPlayBean = videoPlayBean
                    // 有部分视频不存在flv接口下的mp4，无法提供播放服务，需要及时通知。
                    if (videoPlayBean.code != 0) {
                        // 弹出通知弹窗
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
                        val dashVideoPlayBean = networkService.n10(bvid, cid)
                        if (dashVideoPlayBean.code != 0) {
                            setAsJzvdConfig(videoPlayBean.data.durl[0].url, "")
                        }

                        dashVideoPlayBean.data.dash.video[0].also {
                            if (it.width < it.height) {
                                // 竖屏
                                binding.asVideoAppbar.updateLayoutParams<ViewGroup.LayoutParams> {
                                    height = windowManager.defaultDisplay.height / 4 * 3
                                }
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
        }
    }

    /**
     * 加载番剧视频列表信息
     *
     */
    private fun loadBangumiVideoList() {
        launchIO {
            val bangumiSeasonBean = networkService.n13(epid)
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
                },
            ).show()
        } else {
            loadVideoPlay("bangumi")
        }
    }

    /**
     * 加载视频列表信息
     *
     */
    private fun loadVideoList() {
        launchIO {
            val videoPlayListData = networkService.n15(bvid)

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
        // 设置弹幕监听器
        setAsDanmakuCallback()
    }

    /**
     * 加载用户卡片
     * @param mid Long
     */
    private fun loadUserCardData(mid: Long) {
        launchUI {
            val userCardBean = networkService.n14(mid)
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
        }
        if (asJzvdStd.state == Jzvd.STATE_PLAYING) { // 暂停视频
            asJzvdStd.startButton.performClick()
            changeFaButtonToPlay()
        }
        // 百度统计
        StatService.onPause(this)
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
                    asDanmaku.prepare(
                        BiliDanmukuUtil.createParser(this@AsVideoActivity),
                        BiliDanmukuUtil.setDanmakuContext(),
                    )
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
    /**
     * 配置弹幕监听器
     */
    private fun setAsDanmakuCallback() {
        asDanmaku.setCallback(object : master.flame.danmaku.controller.DrawHandler.Callback {
            override fun prepared() {
                // 弹幕准备好的时候回调，这里启动弹幕
                asDanmaku.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) = Unit

            override fun danmakuShown(danmaku: BaseDanmaku?) = Unit

            override fun drawingFinished() = Unit
        })
    }

    // ——————————————————————————————————————————————————————————————————————————
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

    companion object {
        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }
    }
}
