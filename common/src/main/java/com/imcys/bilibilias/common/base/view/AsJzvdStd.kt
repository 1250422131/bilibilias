package com.imcys.bilibilias.common.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import cn.jzvd.JZUtils
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.R
import com.microsoft.appcenter.analytics.Analytics
import master.flame.danmaku.controller.IDanmakuView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


interface JzbdStdInfo {
    fun statePlaying(state: Int)
    fun stopPlay(state: Int)
    fun endPlay(state: Int)
    fun seekBarStopTracking(state: Int)
}

/**
 * 继承饺子播放器，这里需要定义一些东西
 * @constructor
 */
open class AsJzvdStd : JzvdStd {


    private lateinit var jzbdStdInfo: JzbdStdInfo
    var stopTime: Long = 0
    var asDanmaku: IDanmakuView = findViewById(R.id.as_jzvdstd_DanmakuView)
    private var startLinearLayout: LinearLayout = findViewById(R.id.start_layout)
    protected val asJzvdstdPosterFL: FrameLayout = findViewById(R.id.as_jzvdstd_poster_fl)
    private var asJzvdstdPicDlBt: TextView = findViewById(R.id.as_jzvdstd_pic_dl_bt)

    var posterImageUrl: String? = ""


    fun setPlayStateListener(jzbdStdInfo: JzbdStdInfo) {
        this.jzbdStdInfo = jzbdStdInfo
    }

    fun updatePoster(url: String) {
        posterImageUrl = url
        Glide.with(this.context)
            .load(url)
            .into(this.posterImageView)
    }


    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AsJzvdStd)
        val showPlayType = typedArray.getBoolean(R.styleable.AsJzvdStd_showPlayButton, true)
        if (!showPlayType) startLinearLayout.visibility = View.INVISIBLE
        asJzvdstdPicDlBt.setOnClickListener {
            downloadPic()
            //通知下载成功
            Analytics.trackEvent("下载封面")
        }


    }


    constructor(context: Context) : super(context)

    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
    }


    override fun onStateAutoComplete() {
        super.onStateAutoComplete()

        changeUiToComplete()
        cancelDismissControlViewTimer()
        bottomProgressBar.progress = 100
        asJzvdstdPosterFL.isVisible = true

        //通知播放完成
        jzbdStdInfo.endPlay(state)
    }


    override fun onClick(v: View) {
        super.onClick(v)
        val i = v.id
        if (i == R.id.poster) {
            clickPoster()
        } else if (i == R.id.surface_container) {
            clickSurfaceContainer()
            if (clarityPopWindow != null) {
                clarityPopWindow.dismiss()
            }
        } else if (i == R.id.back) {
            clickBack()
        } else if (i == R.id.back_tiny) {
            clickBackTiny()
        } else if (i == R.id.clarity) {
            clickClarity()
        } else if (i == R.id.retry_btn) {
            clickRetryBtn()
        } else if (i == R.id.as_jzvdstd_pic_dl_bt) {
            clickPicDownload()
        }
    }

    private fun clickPicDownload() {

        if (posterImageUrl != "") {
            downloadPic()
        }


    }

    private fun downloadPic() {


        Glide.with(this.context).asBitmap().load(posterImageUrl)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?,
                ) {
                    val photoDir = File(Environment.getExternalStorageDirectory(), "BILIBILIAS")
                    if (!photoDir.exists()) {
                        photoDir.mkdirs()
                    }
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val photo = File(photoDir, fileName)
                    try {
                        val fos = FileOutputStream(photo)
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        fos.flush()
                        fos.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    //通知相册感谢
                    updatePhotoMedia(photo, this@AsJzvdStd.context)
                    asToast(this@AsJzvdStd.context, "已经储存到相册了")
                }
            })

    }

    //更新图库
    private fun updatePhotoMedia(file: File, context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file)
        context.sendBroadcast(intent)
    }

    override fun clickPoster() {
        if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.currentUrl == null) {
            Toast.makeText(
                jzvdContext,
                resources.getString(cn.jzvd.R.string.no_url),
                Toast.LENGTH_SHORT
            )
                .show()
            return
        } else if (state == STATE_AUTO_COMPLETE) {
            onClickUiToggle()
        }

        /*
        暂停使用海报播放
        if (state == STATE_NORMAL) {
            if (!jzDataSource.currentUrl.toString().startsWith("file") &&
                !jzDataSource.currentUrl.toString().startsWith("/") &&
                !JZUtils.isWifiConnected(jzvdContext) && !WIFI_TIP_DIALOG_SHOWED
            ) {
                showWifiDialog()
                return
            }
            onPlayStartListener(state)
            startVideo()
        }*/
    }

    override fun gotoFullscreen() {

        gotoFullscreenTime = System.currentTimeMillis()
        var vg = parent as ViewGroup
        jzvdContext = vg.context
        blockLayoutParams = layoutParams
        blockIndex = vg.indexOfChild(this)
        blockWidth = width
        blockHeight = height

        vg.removeView(this)
        cloneAJzvd(vg)
        CONTAINER_LIST.add(vg)
        vg = JZUtils.scanForActivity(jzvdContext).window.decorView as ViewGroup

        val fullLayout: ViewGroup.LayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        vg.addView(this, fullLayout)

        setScreenFullscreen()
        JZUtils.hideStatusBar(jzvdContext)

        if (isHorizontalAsVideo()) {
            JZUtils.setRequestedOrientation(jzvdContext, FULLSCREEN_ORIENTATION)
        } else {
            JZUtils.setRequestedOrientation(jzvdContext, NORMAL_ORIENTATION)
        }
        JZUtils.hideSystemUI(jzvdContext) //华为手机和有虚拟键的手机全屏时可隐藏虚拟键 issue:1326

    }

    private fun isHorizontalAsVideo(): Boolean {
        val picImage = findViewById<ImageView>(R.id.poster)
        val imageWidth = picImage.width
        val imageHeight = picImage.height

        return width > height


    }


    override fun onStatePlaying() {
        //先一步返回状态，确保外部明确因为什么原因开启了播放
        asJzvdstdPosterFL.isVisible = false
        jzbdStdInfo.statePlaying(state)
        super.onStatePlaying()

    }


    override fun onStopTrackingTouch(seekBar: SeekBar) {
        super.onStopTrackingTouch(seekBar)
        jzbdStdInfo.seekBarStopTracking(state)
    }


    override fun onStatePause() {
        //记录暂停时间
        asJzvdstdPosterFL.isVisible = true
        stopTime = currentPositionWhenPlaying
        jzbdStdInfo.stopPlay(state)
        super.onStatePause()
    }


}