package com.imcys.bilibilias.base.helper

import android.content.Context
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout

class SplashHelper : ConstraintHelper {


    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context?, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    override fun updatePostLayout(container: ConstraintLayout?) {
        super.updatePostLayout(container)
        val views = getViews(container)
        views.forEach {
            val anim = ViewAnimationUtils.createCircularReveal(it, 0, 0, 0f, it.width.toFloat())
            anim.duration = 1000
            anim.start()
        }
    }
}