package com.imcys.player

import android.content.Context
import android.view.Surface
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSink
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.TransferListener
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import coil.load
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import java.io.File
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class AsGSYVideoPlayer(context: Context) : StandardGSYVideoPlayer(context), ExoMediaSourceInterceptListener {

    @Inject
    lateinit var dataSource: DataSource.Factory

    init {
        initConfig()
    }

    private fun initConfig() {
        // 是否可以滑动调整
        mIsTouchWiget = true
        // 长时间失去音频焦点，暂停播放器
        isReleaseWhenLossAudio = false
        // 增加title
        titleTextView.isVisible = true
        // 设置返回键
        backButton.isVisible = true
    }

    fun setUp(url: String?, title: String, pic: String): Boolean {
        thumbImageView = setThumbImageView(pic)
        return setUp(url, false, title)
    }

    private fun setThumbImageView(pic: String): ImageView = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        load(pic)
    }

    override fun setVideoAllCallBack(mVideoAllCallBack: VideoAllCallBack?) {
        super.setVideoAllCallBack(mVideoAllCallBack)
    }

    private var mergingMediaSource: MediaSource? = null
    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun setMediaSource(videoUrl: com.imcys.model.Dash.Video, audioUrl: com.imcys.model.Dash.Audio) {
        // todo 设置了这个 Decoder init failed: c2.android.av1.decoder, Format(1, null, null, video/av01, null, -1, null, [3840, 2160, -1.0, null], [-1, -1])
        val videoType = MimeTypes.getMediaMimeType(videoUrl.codecs)
        val audioType = MimeTypes.getMediaMimeType(audioUrl.codecs)

        val videoSource = createMediaSource(dataSource, videoUrl.baseUrl)
        val audioSource = createMediaSource(dataSource, audioUrl.baseUrl)

        mergingMediaSource = MergingMediaSource(true, false, videoSource, audioSource)
        ExoSourceManager.setExoMediaSourceInterceptListener(this)
    }

    override fun getMediaSource(
        dataSource: String?,
        preview: Boolean,
        cacheEnable: Boolean,
        isLooping: Boolean,
        cacheDir: File?
    ): MediaSource? = mergingMediaSource

    override fun getHttpDataSourceFactory(
        userAgent: String?,
        listener: TransferListener?,
        connectTimeoutMillis: Int,
        readTimeoutMillis: Int,
        mapHeadData: MutableMap<String, String>?,
        allowCrossProtocolRedirects: Boolean
    ): DataSource.Factory = dataSource

    override fun cacheWriteDataSinkFactory(CachePath: String?, url: String?): DataSink.Factory? = null
    override fun setUp(url: String?, cacheWithPlay: Boolean, title: String?): Boolean {
        return super.setUp(url, cacheWithPlay, title)
    }

    var currentPosition = 0L
        private set
    var progress: Int = 0
        get() = mProgressBar.progress
        private set

    override fun onLossAudio() {
        super.onLossAudio()
    }

    override fun onAutoCompletion() {
        super.onAutoCompletion()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onSurfaceUpdated(surface: Surface?) {
        super.onSurfaceUpdated(surface)
    }

    override fun onVideoPause() {
        super.onVideoPause()
        Timber.d("进度=4$progress,$currentPosition,$currentState")
        currentPosition = gsyVideoManager.currentPosition
    }
}
