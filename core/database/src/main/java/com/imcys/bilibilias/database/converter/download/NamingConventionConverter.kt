package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.NamingConventionInfo
import org.json.JSONObject

class NamingConventionConverter {
    @TypeConverter
    fun fromString(value: String?): NamingConventionInfo? {
        return value?.let {
            val jsonObject = JSONObject(value)
            val ruleType = runCatching {  jsonObject.getInt("ruleType") }.getOrNull()
            when (ruleType) {
                0 -> {
                    NamingConventionInfo.Video(
                        title = jsonObject.optString("title"),
                        pTitle = jsonObject.optString("pTitle"),
                        author = jsonObject.optString("author"),
                        bvId = jsonObject.optString("bvId"),
                        aid = jsonObject.optString("aid"),
                        cid = jsonObject.optString("cid"),
                    )
                }

                1 -> {
                    NamingConventionInfo.Donghua(
                        title = jsonObject.optString("title"),
                        episodeTitle = jsonObject.optString("episodeTitle"),
                        episodeNumber = jsonObject.optString("episodeNumber"),
                        cid = jsonObject.optString("cid"),
                    )
                }

                else -> null
            }
        }
    }

    @TypeConverter
    fun stringToDownloadStage(namingConventionInfo: NamingConventionInfo?): String? {
        return JSONObject().apply {
            namingConventionInfo?.let {
                put("ruleType", it.ruleType)
                when (it) {
                    is NamingConventionInfo.Video -> {
                        put("title", it.title)
                        put("pTitle", it.pTitle)
                        put("author", it.author)
                        put("bvId", it.bvId)
                        put("aid", it.aid)
                        put("cid", it.cid)
                    }

                    is NamingConventionInfo.Donghua -> {
                        put("title", it.title)
                        put("episodeTitle", it.episodeTitle)
                        put("episodeNumber", it.episodeNumber)
                        put("cid", it.cid)
                    }
                }
            }
        }.toString()
    }
}