package com.imcys.bilibilias.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.asLogD
import master.flame.danmaku.controller.IDanmakuView


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
class AsJzvdStd : JzvdStd {



    private lateinit var jzbdStdInfo: JzbdStdInfo
    var stopTime: Long = 0
    var asDanmaku: IDanmakuView = findViewById(R.id.as_jzvdstd_DanmakuView)
    private var startLinearLayout: LinearLayout = findViewById(R.id.start_layout)


    fun setPlayStateListener(jzbdStdInfo: JzbdStdInfo) {
        this.jzbdStdInfo = jzbdStdInfo
    }


    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AsJzvdStd)
        val showPlayType = typedArray.getBoolean(R.styleable.AsJzvdStd_showPlayButton, true)
        if (!showPlayType) startLinearLayout.visibility = View.INVISIBLE

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
        }
    }


    override fun clickPoster() {
        if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.currentUrl == null) {
            Toast.makeText(jzvdContext,
                resources.getString(cn.jzvd.R.string.no_url),
                Toast.LENGTH_SHORT)
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


    override fun onStatePlaying() {
        //先一步返回状态，确保外部明确因为什么原因开启了播放
        jzbdStdInfo.statePlaying(state)
        super.onStatePlaying()

    }


    override fun onStatePreparingPlaying() {
        super.onStatePreparingPlaying()

    }


    override fun onStopTrackingTouch(seekBar: SeekBar) {
        super.onStopTrackingTouch(seekBar)
        jzbdStdInfo.seekBarStopTracking(state)
    }


    override fun onStatePause() {
        //记录暂停时间
        stopTime = currentPositionWhenPlaying
        jzbdStdInfo.stopPlay(state)
        super.onStatePause()
    }


}