package com.imcys.bilibilias.core.player

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSink
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.TransferListener
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.kuaishou.akdanmaku.DanmakuConfig
import com.kuaishou.akdanmaku.render.SimpleRenderer
import com.kuaishou.akdanmaku.render.TypedDanmakuRenderer
import com.kuaishou.akdanmaku.ui.DanmakuPlayer
import com.kuaishou.akdanmaku.ui.DanmakuView
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import dagger.hilt.android.AndroidEntryPoint
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import java.io.File
import javax.inject.Inject
import javax.inject.Qualifier

@UnstableApi
@AndroidEntryPoint
class DanmakuVideoPlayer : StandardGSYVideoPlayer, ExoMediaSourceInterceptListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, fullFlag: Boolean) : super(context, fullFlag)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun getLayoutId() = R.layout.feature_player_danmaku_layout

    private val simpleRenderer = SimpleRenderer()
    private val renderer = TypedDanmakuRenderer(simpleRenderer)
    private val danmakuPlayer: DanmakuPlayer = DanmakuPlayer(renderer)
    private lateinit var danmakuView: DanmakuView

    private var mergingMediaSource: MediaSource? = null

    @Inject
    private lateinit var dataSource: DataSource.Factory

    override fun init(context: Context) {
        super.init(context)
        findViewById<DanmakuView>(R.id.danmaku_view).apply {
            danmakuView = this
            this@DanmakuVideoPlayer.danmakuPlayer.bindView(this)
        }
        ExoSourceManager.setExoMediaSourceInterceptListener(this)
    }

    fun setMediaSource(videoUrl: String, audioUrl: String) {
        val videoSource = createMediaSource(videoUrl)
        val audioSource = createMediaSource(audioUrl)

        mergingMediaSource = MergingMediaSource(true, false, videoSource, audioSource)
    }

    override fun onPrepared() {
        super.onPrepared()
        onPrepareDanmaku(this)
    }

    override fun onVideoPause() {
        super.onVideoPause()
        danmakuOnPause()
    }

    override fun onVideoResume(isResume: Boolean) {
        super.onVideoResume(isResume)
        danmakuOnResume()
    }

    override fun clickStartIcon() {
        super.clickStartIcon()
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            danmakuOnResume()
        } else if (mCurrentState == CURRENT_STATE_PAUSE) {
            danmakuOnPause()
        }
    }

    override fun onCompletion() {
        super.onCompletion()
        releaseDanmaku(this)
    }

    override fun onSeekComplete() {
        super.onSeekComplete()
        val time = mProgressBar.progress * duration / 100
        // 如果已经初始化过的，直接seek到对于位置
//        if (mHadPlay && getDanmakuView() != null && getDanmakuView().isPrepared()) {
//            resolveDanmakuSeek(this, time)
//        } else if (mHadPlay && getDanmakuView() != null && !getDanmakuView().isPrepared()) {
//            //如果没有初始化过的，记录位置等待
//            setDanmakuStartSeekPosition(time)
//        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
//            R.id.send_danmaku -> addDanmaku(true)
//            R.id.toogle_danmaku -> {
//                mDanmaKuShow = !mDanmaKuShow
//                resolveDanmakuShow()
//            }
        }
    }

    override fun cloneParams(from: GSYBaseVideoPlayer, to: GSYBaseVideoPlayer) {
//        (to as DanmakuVideoPlayer).mDumakuFile = (from as DanmakuVideoPlayer).mDumakuFile
        super.cloneParams(from, to)
    }

    /**
     * 处理播放器在全屏切换时，弹幕显示的逻辑
     * 需要格外注意的是，因为全屏和小屏，是切换了播放器，所以需要同步之间的弹幕状态
     */
    override fun startWindowFullscreen(
        context: Context?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer? {
        val gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar)
        if (gsyBaseVideoPlayer != null) {
            val gsyVideoPlayer = gsyBaseVideoPlayer as DanmakuVideoPlayer
            // 对弹幕设置偏移记录
//            gsyVideoPlayer.setDanmakuStartSeekPosition(currentPositionWhenPlaying)
//            gsyVideoPlayer.setDanmaKuShow(getDanmaKuShow())
            onPrepareDanmaku(gsyVideoPlayer)
        }
        return gsyBaseVideoPlayer
    }

    /**
     * 处理播放器在退出全屏时，弹幕显示的逻辑
     * 需要格外注意的是，因为全屏和小屏，是切换了播放器，所以需要同步之间的弹幕状态
     */
    override fun resolveNormalVideoShow(
        oldF: View?,
        vp: ViewGroup?,
        gsyVideoPlayer: GSYVideoPlayer?
    ) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)
        if (gsyVideoPlayer != null) {
            val gsyDanmaVideoPlayer = gsyVideoPlayer as DanmakuVideoPlayer
//            setDanmaKuShow(gsyDanmaVideoPlayer.getDanmaKuShow())
//            if (gsyDanmaVideoPlayer.getDanmakuView() != null &&
//                gsyDanmaVideoPlayer.getDanmakuView().isPrepared()
//            ) {
//                resolveDanmakuSeek(this, gsyDanmaVideoPlayer.currentPositionWhenPlaying)
//                resolveDanmakuShow()
//                releaseDanmaku(gsyDanmaVideoPlayer)
//            }
        }
    }

    protected fun danmakuOnPause() {
//        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
//            mDanmakuView.pause()
//        }
    }

    protected fun danmakuOnResume() {
//        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
//            mDanmakuView.resume()
//        }
    }

    fun setDanmaKuStream(`is`: File) {
//        mDumakuFile = `is`
//        if (!getDanmakuView().isPrepared()) {
//            onPrepareDanmaku(currentPlayer as DanmakuVideoPlayer)
//        }
    }

    private fun initDanmaku() {
        // https://github.com/KwaiAppTeam/AkDanmaku/blob/master/library/src/main/java/com/kuaishou/akdanmaku/DanmakuConfig.kt
        val config = DanmakuConfig()
        danmakuPlayer.updateConfig(config)
    }

    /**
     * 弹幕的显示与关闭
     */
    private fun resolveDanmakuShow() {
//        post {
//            if (mDanmaKuShow) {
//                if (!getDanmakuView().isShown()) getDanmakuView().show()
//                mToogleDanmaku.setText("弹幕关")
//            } else {
//                if (getDanmakuView().isShown()) {
//                    getDanmakuView().hide()
//                }
//                mToogleDanmaku.setText("弹幕开")
//            }
//        }
    }

    /**
     * 开始播放弹幕
     */
    private fun onPrepareDanmaku(gsyVideoPlayer: DanmakuVideoPlayer) {
//        if (gsyVideoPlayer.getDanmakuView() != null &&
//            !gsyVideoPlayer.getDanmakuView().isPrepared() &&
//            gsyVideoPlayer.getParser() != null
//        ) {
//            gsyVideoPlayer.getDanmakuView().prepare(
//                gsyVideoPlayer.getParser(),
//                gsyVideoPlayer.getDanmakuContext()
//            )
//        }
    }

    /**
     * 弹幕偏移
     */
    private fun resolveDanmakuSeek(gsyVideoPlayer: DanmakuVideoPlayer, time: Long) {
    }

    /**
     * 释放弹幕控件
     */
    private fun releaseDanmaku(danmakuVideoPlayer: DanmakuVideoPlayer?) {
//        if (danmakuVideoPlayer?.getDanmakuView() != null) {
//            Debuger.printfError("release Danmaku!")
//            danmakuVideoPlayer.getDanmakuView().release()
//        }
    }

    /**
     * 模拟添加弹幕数据
     */
    private fun addDanmaku(islive: Boolean) {
    }

    private fun createMediaSource(url: String) = ProgressiveMediaSource.Factory(dataSource)
        .createMediaSource(
            MediaItem.Builder()
                .setUri(url)
                .build()
        )

    @OptIn(UnstableApi::class)
    override fun getMediaSource(
        dataSource: String?,
        preview: Boolean,
        cacheEnable: Boolean,
        isLooping: Boolean,
        cacheDir: File?
    ): MediaSource? = mergingMediaSource

    @OptIn(UnstableApi::class)
    override fun getHttpDataSourceFactory(
        userAgent: String?,
        listener: TransferListener?,
        connectTimeoutMillis: Int,
        readTimeoutMillis: Int,
        mapHeadData: MutableMap<String, String>?,
        allowCrossProtocolRedirects: Boolean
    ): DataSource.Factory? = dataSource

    @OptIn(UnstableApi::class)
    override fun cacheWriteDataSinkFactory(CachePath: String?, url: String?): DataSink.Factory? =
        null
}
