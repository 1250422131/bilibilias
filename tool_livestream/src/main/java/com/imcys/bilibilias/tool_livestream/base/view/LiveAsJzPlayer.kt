package com.imcys.bilibilias.tool_livestream.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.imcys.bilibilias.common.base.view.AsJzvdStd
import com.imcys.bilibilias.tool_livestream.R

class LiveAsJzPlayer : AsJzvdStd {

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun getLayoutId(): Int {
        return R.layout.livestream_jz_layout_std
    }

}