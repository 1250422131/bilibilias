package com.imcys.bilibilias.home.ui.activity.video

import android.content.Context
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.danmaku.BiliDanmukuParser
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import java.io.File

object BiliDanmukuUtil {
    fun saveDmTempFile(content: ByteArray) {
        getDmTempFile(BaseApplication.applicationContext()).writeBytes(content)
    }

    fun getDmTempFile(context: Context): File {
        return File(context.cacheDir.path, "temp_dm.xml")
    }

    fun createParser(context: Context): BaseDanmakuParser {
        val loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)
        loader.load(getDmTempFile(context).inputStream())

        val parser = BiliDanmukuParser()
        parser.load(loader.dataSource)
        return parser
    }

    fun setDanmakuContext(): DanmakuContext {
        val danmakuContext = DanmakuContext.create()

        val overlappingEnablePair = mutableMapOf<Int, Boolean>(
            BaseDanmaku.TYPE_SCROLL_LR to true,
            BaseDanmaku.TYPE_FIX_BOTTOM to true
        )
        // 设置描边样式
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F)
            // 是否启用合并重复弹幕
            .setDuplicateMergingEnabled(false)
            // 设置弹幕滚动速度系数,只对滚动弹幕有效
            .setScrollSpeedFactor(1.2f)
            .setScaleTextSize(1.2f)
            // 设置最大显示行数
            .setMaximumLines(mutableMapOf())
            // 设置防弹幕重叠，null为允许重叠
            .preventOverlapping(overlappingEnablePair)
        return danmakuContext
    }
}
