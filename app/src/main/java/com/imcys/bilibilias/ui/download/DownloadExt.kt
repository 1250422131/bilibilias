package com.imcys.bilibilias.ui.download

import com.tonyodev.fetch2.Download

val Download.cid: Long
    get() {
        val cid = extras.getLong(EXTRAS_CID, Long.MIN_VALUE)
        check(cid != Long.MIN_VALUE) { "extra 中没有 cid" }
        return cid
    }
val Download.title: String
    get() {
        val title = extras.getString(EXTRAS_TITLE, "")
        check(title != "") { "extra 中没有 title" }
        return title
    }
val Download.bvid: String
    get() {
        val bvid = extras.getString(EXTRAS_BV_ID, "")
        check(bvid != "") { "extra 中没有 bvid" }
        return bvid
    }
