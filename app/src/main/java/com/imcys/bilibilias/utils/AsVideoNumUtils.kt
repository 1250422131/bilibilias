package com.imcys.bilibilias.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.imcys.bilibilias.home.ui.model.VideoPageListData
import okhttp3.OkHttpClient
import okhttp3.Request


object AsVideoNumUtils {

    fun getBvid(string: String): String {

        //bv过滤
        var epRegex =
            Regex("""(BV|bv|Bv|bV)1([A-z]|[0-9]){2}4([A-z]|[0-9])1([A-z]|[0-9])7([A-z]|[0-9]){2}""")
        if (epRegex.containsMatchIn(string)) {
            return epRegex.find(string)?.value.toString()
        }

        //av过滤
        epRegex = Regex("""(?<=(av|aV|AV|Av))([0-9]+)""")
        if (epRegex.containsMatchIn(string)) return VideoNumConversion.toBvidOffline(epRegex.find(
            string)?.value!!.toInt())


        return ""
    }




}