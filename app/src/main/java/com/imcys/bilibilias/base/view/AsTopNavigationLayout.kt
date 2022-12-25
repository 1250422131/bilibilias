package com.imcys.bilibilias.base.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.imcys.bilibilias.R

class AsTopNavigationLayout(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    init {
        val view =  LayoutInflater.from(context).inflate(R.layout.as_top_navigationlayout, this)
        val asTopNavigationLayoutBack = view.findViewById<ImageView>(R.id.as_top_navigation_layout_back)
        asTopNavigationLayoutBack.setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }

    }

}