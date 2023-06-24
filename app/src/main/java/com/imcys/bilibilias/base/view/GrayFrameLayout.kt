package com.imcys.bilibilias.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.imcys.bilibilias.R


class GrayFrameLayout : FrameLayout {
    // 画笔
    private val mPaint: Paint = Paint()

    // 判断是否使用灰度图层做滤镜
    private var isGrayType = false

    private val graylessSubviews = mutableListOf<View>()

    constructor(context: Context) : super(context)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {


        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GrayFrameLayout)
        isGrayType = typedArray.getBoolean(R.styleable.GrayFrameLayout_isGray, false)
        //判断是否灰度化
        if (isGrayType) {
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            // 使用Paint.setColorFilter()方法设置颜色过滤器
            //还可以对其颜色、透明度、混合模式等属性进行更改
            mPaint.colorFilter = ColorMatrixColorFilter(cm)
            // 解析graylessSubviews属性的值
            val graylessSubviews =
                typedArray.getResourceId(R.styleable.GrayFrameLayout_graylessSubviews, 0)
        }


    }


    // 在父控件调用它的子控件的draw()后执行
    // 因此这里绘制子控件
    override fun dispatchDraw(canvas: Canvas) {

        if (isGrayType) canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)


        // 调用父类的dispatchDraw()方法，完成控件的子控件绘制
        // 调用restore()方法会销毁新的图层，但是新图层中应用的滤镜效果并不会丢失。
        // 这是因为，在Android中，图层是以离屏缓冲（Offscreen Buffer）的形式实现的，也就是说，新的图层是在内存中创建的，它不会直接显示在屏幕上。
        // 因此，即使新的图层被销毁了，它的内容也会被保存下来，并在下一次控件被渲染时应用滤镜效果。
        // 调用父类的dispatchDraw()方法，完成控件的子控件绘制

        super.dispatchDraw(canvas)

        if (isGrayType) canvas.restore()


    }

    // View的整个生命周期中只会被调用一次，用于渲染View本身
    override fun draw(canvas: Canvas) {
        if (isGrayType) canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)

        //去除部分组件灰度化
        drawGraylessSubview(canvas)

        super.draw(canvas)
        if (isGrayType) canvas.restore()


    }


    //添加去除灰度化组件
    fun addGrayLessSubviews(graylessSubview: View) {
        graylessSubviews.add(graylessSubview)
    }

    private fun drawGraylessSubview(canvas: Canvas) {
        // 绘制需要去掉灰度化的子控件

        graylessSubviews.forEach {
            // 保存当前的绘制状态
            canvas.save()
            // 绘制子控件
            drawChild(canvas, it, drawingTime)
            // 恢复之前保存的绘制状态
            canvas.restore()

        }
    }

}