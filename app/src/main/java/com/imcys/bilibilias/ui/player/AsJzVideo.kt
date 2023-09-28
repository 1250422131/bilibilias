package com.imcys.bilibilias.ui.player

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.slider.Slider
import com.google.android.material.textview.MaterialTextView
import com.imcys.bilibilias.R

class AsJzVideo : JzvdStd {
    private lateinit var ivPlayPause: ShapeableImageView
    private lateinit var ivOpenCloseDanmu: ShapeableImageView
    private lateinit var ivFullScreen: ShapeableImageView

    private lateinit var slider: Slider

    private lateinit var tvTimeCurrentAndTotal: MaterialTextView

    // 清晰度
    private lateinit var tvClarity: MaterialTextView

    private lateinit var videoControl: ConstraintLayout
    override fun getLayoutId(): Int = R.layout.app_as_video_jz_std

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    override fun init(context: Context?) {
        super.init(context)
        initView()
        initListener()
    }

    private fun initView() {
        startButton = findViewById(R.id.iv_play_pause)

        ivPlayPause = findViewById(R.id.iv_play_pause)
        ivOpenCloseDanmu = findViewById(R.id.iv_open_close_danmu)
        ivFullScreen = findViewById(R.id.iv_fullscreen)
        slider = findViewById(R.id.slider)
        tvTimeCurrentAndTotal = findViewById(R.id.tv_time_current_and_total)
        tvClarity = findViewById(R.id.tv_clarity)
        videoControl = findViewById(R.id.video_control)
    }

    private fun initListener() {
        ivPlayPause.setOnClickListener(this)

    }

    override fun getDuration(): Long {
        currentPositionWhenPlaying
        return super.getDuration()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        super.onProgressChanged(seekBar, progress, fromUser)
    }

    override fun onStatePlaying() {
        super.onStatePlaying()
        ivPlayPause.setImageResource(R.drawable.ic_as_video_pause)
    }

    override fun onStatePause() {
        super.onStatePause()
        ivPlayPause.setImageResource(R.drawable.ic_as_video_play)
    }

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
        changeUiToComplete()
        cancelDismissControlViewTimer()
        bottomProgressBar.progress = 100
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play_pause -> {
                when (state) {
                    Jzvd.STATE_NORMAL, Jzvd.STATE_AUTO_COMPLETE -> {
                        // 播放视频
                        startVideo()
                    }

                    Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                        // 恢复播放/暂停播放
                        startButton.performClick()
                    }
                }
            }

            else -> {}
        }
    }
}

enum class VideoState {
    STATE_IDLE,
    STATE_NORMAL,
    STATE_PREPARING,
    STATE_PREPARING_CHANGE_URL,
    STATE_PREPARING_PLAYING,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSE,
    STATE_AUTO_COMPLETE,
    STATE_ERROR,
}
