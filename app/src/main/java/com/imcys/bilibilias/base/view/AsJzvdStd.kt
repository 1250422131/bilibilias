package com.imcys.bilibilias.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import cn.jzvd.JZUtils
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.R


/**
 * 继承饺子播放器，这里需要定义一些东西
 * @constructor
 */
class AsJzvdStd : JzvdStd {


    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AsJzvdStd)
        val showPlayType = typedArray.getBoolean(R.styleable.AsJzvdStd_showPlayButton, true)

        val startLayout = findViewById<LinearLayout>(R.id.start_layout)
        if (!showPlayType) startLayout.visibility = View.GONE

    }

    constructor(context: Context) : super(context)

    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
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
        }
        if (state == STATE_NORMAL) {
            if (!jzDataSource.currentUrl.toString().startsWith("file") &&
                !jzDataSource.currentUrl.toString().startsWith("/") &&
                !JZUtils.isWifiConnected(jzvdContext) && !WIFI_TIP_DIALOG_SHOWED
            ) {
                showWifiDialog()
                return
            }
            startVideo()
        } else if (state == STATE_AUTO_COMPLETE) {
            onClickUiToggle()
        }
    }


}