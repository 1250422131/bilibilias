package com.imcys.bilibilias.ui.player

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSink
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.TransferListener
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import coil.load
import com.imcys.bilibilias.base.utils.getActivity
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.model.video.Dash
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import okhttp3.OkHttpClient
import org.chromium.net.CronetEngine
import timber.log.Timber
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import java.io.File
import java.util.concurrent.Executors

class AsGSYVideoPlayer(context: Context) : StandardGSYVideoPlayer(context) {
    init {
        val orientationUtils = OrientationUtils(context.getActivity(), this)
        fullscreenButton.setOnClickListener {
            // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
            // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
            orientationUtils.resolveByClick()
        }

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

    override fun setUp(url: String?, cacheWithPlay: Boolean, title: String?): Boolean {
        return super.setUp(url, cacheWithPlay, title)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun setMediaSource(http: OkHttpClient, videoUrl: Dash.Video?, audioUrl: Dash.Audio?) {
        val dataSourceFactory = OkHttpDataSource.Factory(http)
        val cronetEngine = CronetEngine.Builder(context)
            .enableQuic(true)
            .enableBrotli(true)
            .enableHttp2(true)
            .setUserAgent(BROWSER_USER_AGENT)
            .build()
        val videoType = MimeTypes.getMediaMimeType(videoUrl?.codecs)
        val audioType = MimeTypes.getMediaMimeType(audioUrl?.codecs)
        val cronetDataSource = CronetDataSource.Factory(cronetEngine, Executors.newFixedThreadPool(4))
        val videoSource = createMediaSource(cronetDataSource, videoUrl?.baseUrl ?: "", videoType)
        val audioSource = createMediaSource(cronetDataSource, audioUrl?.baseUrl ?: "", audioType)
        ExoSourceManager.setExoMediaSourceInterceptListener(object : ExoMediaSourceInterceptListener {
            override fun getMediaSource(
                dataSource: String?,
                preview: Boolean,
                cacheEnable: Boolean,
                isLooping: Boolean,
                cacheDir: File?
            ): MediaSource {
                val contentUri = Uri.parse(dataSource)
                val contentType: Int = Util.inferContentType(contentUri)
                // Type =  4
                // when (contentType) {
                // HlsMediaSource.Factory()
                // DashMediaSource.Factory(DataSource.Factory {mergeSource })
                //     C.CONTENT_TYPE_HLS -> return HlsMediaSource.Factory(CustomSourceTag.getDataSourceFactory(this@GSYApplication.getApplicationContext(), preview))
                // //         .createMediaSource(contentUri)
                // }
                // CONTENT_TYPE_OTHER
                Timber.tag("getMediaSource").d("contentUri=$contentType,$contentUri,")
                val mergingMediaSource = MergingMediaSource(videoSource, audioSource)
                return mergingMediaSource
            }

            override fun getHttpDataSourceFactory(
                userAgent: String?,
                listener: TransferListener?,
                connectTimeoutMillis: Int,
                readTimeoutMillis: Int,
                mapHeadData: MutableMap<String, String>?,
                allowCrossProtocolRedirects: Boolean
            ): DataSource.Factory? = null

            override fun cacheWriteDataSinkFactory(CachePath: String?, url: String?): DataSink.Factory? = null
        })
    }
}
