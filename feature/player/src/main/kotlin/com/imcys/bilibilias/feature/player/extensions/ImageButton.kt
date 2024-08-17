package com.imcys.bilibilias.feature.player.extensions

import android.content.Context
import android.widget.ImageButton
import androidx.core.content.ContextCompat

fun ImageButton.setImageDrawable(context: Context, id: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, id))
}
