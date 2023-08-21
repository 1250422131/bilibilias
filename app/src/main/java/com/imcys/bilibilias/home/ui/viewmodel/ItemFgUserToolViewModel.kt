package com.imcys.bilibilias.home.ui.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.home.ui.activity.user.BangumiFollowActivity
import com.imcys.bilibilias.home.ui.activity.user.CollectionActivity
import com.imcys.bilibilias.home.ui.activity.user.PlayHistoryActivity

class ItemFgUserToolViewModel : ViewModel() {
    fun goToCollection(view: View) {
        CollectionActivity.actionStart(view.context)
    }

    fun goToPlayHistory(view: View) {
        PlayHistoryActivity.actionStart(view.context)
    }

    fun goToBangumiFollowr(view: View) {
        BangumiFollowActivity.actionStart(view.context)
    }
}