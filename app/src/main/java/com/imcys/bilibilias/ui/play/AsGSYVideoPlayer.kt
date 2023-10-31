package com.imcys.bilibilias.ui.play

import android.content.Context
import android.view.Surface
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSink
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.TransferListener
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.SingleSampleMediaSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import coil.load
import com.imcys.common.utils.getActivity
import com.imcys.model.Dash
import com.imcys.bilibilias.common.di.AsDispatchers
import com.imcys.bilibilias.common.di.Dispatcher
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import org.chromium.net.CronetEngine
import org.chromium.net.RequestFinishedInfo
import timber.log.Timber
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import java.io.File
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class AsGSYVideoPlayer(context: Context) : StandardGSYVideoPlayer(context), ExoMediaSourceInterceptListener {

    @Inject
    @Dispatcher(AsDispatchers.IO)
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    lateinit var cronetEngine: CronetEngine

    @Inject
    lateinit var cronetDataSource: CronetDataSource.Factory

    init {
        // cronetEngine.startNetLogToFile(context.cacheDir.path + "/netLog.log", false)
        cronetRequestListener()
        val orientationUtils = OrientationUtils(context.getActivity(), this)
        // fullscreenButton.setOnClickListener {
        //     // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
        //     // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
        //     orientationUtils.resolveByClick()
        // }
        initConfig()
    }

    private inner class RequestInfo : RequestFinishedInfo.Listener(ioDispatcher.asExecutor()) {
        override fun onRequestFinished(requestInfo: RequestFinishedInfo) {
            Timber.d("cronet header=${requestInfo.responseInfo?.allHeaders}")
            val exception = requestInfo.exception
            if (exception != null) {
                Timber.e("视频流异常", exception.message)
            }
        }
    }

    private fun cronetRequestListener() {
        cronetEngine.addRequestFinishedListener(RequestInfo())
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
        // /data/data/com.imcys.bilibilias.debug/files/amv.ass
        val uri = File(context.filesDir, "/amv.ass").toUri()

        ConcatenatingMediaSource()
        val subtitle =
            MediaItem.SubtitleConfiguration.Builder(uri)
                .setMimeType(MimeTypes.TEXT_SSA) // The correct MIME type (required).
                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                .setLanguage("")
                .build()
        val bandwidthMeter = DefaultBandwidthMeter.Builder(
            context
        ).setInitialBitrateEstimate(videoUrl.bandwidth.toLong()).build()

        // todo 设置了这个 Decoder init failed: c2.android.av1.decoder, Format(1, null, null, video/av01, null, -1, null, [3840, 2160, -1.0, null], [-1, -1])
        val videoType = MimeTypes.getMediaMimeType(videoUrl.codecs)
        val audioType = MimeTypes.getMediaMimeType(audioUrl.codecs)
        cronetDataSource.setTransferListener(bandwidthMeter)

        val videoSource = createMediaSource(cronetDataSource, videoUrl.baseUrl)
        val audioSource = createMediaSource(cronetDataSource, audioUrl.baseUrl)
        videoSource.maybeThrowSourceInfoRefreshError()
        val factory = DefaultDataSource.Factory(context)
        val subSource = SingleSampleMediaSource.Factory(factory).createMediaSource(subtitle, C.TIME_UNSET)

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
    ): DataSource.Factory = cronetDataSource

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
