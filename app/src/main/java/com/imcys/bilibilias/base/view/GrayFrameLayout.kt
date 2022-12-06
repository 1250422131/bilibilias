package com.imcys.bilibilias.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.imcys.bilibilias.R


class GrayFrameLayout : FrameLayout {
    private val mPaint: Paint = Paint()
    private var isGrayType = false

    constructor(context: Context) : super(context)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GrayFrameLayout)
        isGrayType = typedArray.getBoolean(R.styleable.GrayFrameLayout_isGray, false)

        //判断是否灰度化
        if (isGrayType) {
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            mPaint.colorFilter = ColorMatrixColorFilter(cm)
        }

    }


    override fun dispatchDraw(canvas: Canvas) {
        if (isGrayType) canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        if (isGrayType) canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        if (isGrayType) canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        if (isGrayType) canvas.restore()
    }

}