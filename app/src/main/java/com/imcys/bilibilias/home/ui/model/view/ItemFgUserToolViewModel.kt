package com.imcys.bilibilias.home.ui.model.view

import android.view.View
import com.imcys.bilibilias.home.ui.activity.user.BangumiFollowActivity
import com.imcys.bilibilias.home.ui.activity.user.CollectionActivity
import com.imcys.bilibilias.home.ui.activity.user.PlayHistoryActivity

class ItemFgUserToolViewModel {
    fun goToCollection(view: View) {
        CollectionActivity.actionStart(view.context)
    }

    fun goToPlayHistory(view: View) {
        PlayHistoryActivity.actionStart(view.context)
    }

    fun goToBangumiFollowr(view: View){
        BangumiFollowActivity.actionStart(view.context)
    }
}