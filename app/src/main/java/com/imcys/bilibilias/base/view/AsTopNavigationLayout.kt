package com.imcys.bilibilias.base.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.imcys.bilibilias.R

@SuppressLint("Recycle")
class AsTopNavigationLayout(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AsTopNavigationLayout)
        val view = LayoutInflater.from(context).inflate(R.layout.as_top_navigationlayout, this)
        val title = typedArray.getString(R.styleable.AsTopNavigationLayout_title)

        title?.apply {
            val asTopNavigationTitle = view.findViewById<TextView>(R.id.as_top_navigation_title)
            asTopNavigationTitle.text = this
        }
        val asTopNavigationLayoutBack =
            view.findViewById<ImageView>(R.id.as_top_navigation_layout_back)


        asTopNavigationLayoutBack.setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }

    }

}