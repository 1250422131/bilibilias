package com.imcys.bilibilias.feature.tool.util

import com.imcys.bilibilias.feature.tool.util.InputParseUtil.searchType
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.test.Test

class InputParseUtilTest {

    @Test
    fun searchType_inputText_returnBV() {
        val expectedSearchType = SearchType.BV("BV1Di421Y7Ub")
        expectThat(
            searchType(
                "<iframe src=\"//player.bilibili.com/player.html?isOutside=true&aid=1456179117&bvid=BV1Di421Y7Ub&cid=1610562697&p=1\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"></iframe>",
            ),
        )
            .isA<SearchType.BV>()
            .isEqualTo(expectedSearchType)
        expectThat(
            searchType(
                "【绝区零爆雷？尘白遭T0祸害？原神夏活没可莉？棋一直下丨绝区零攻略？原神卡池分析？不！米游二游吃瓜大杂烩！米游三家争风逆天，尘白社区乱作一团。】 https://www.bilibili.com/video/BV1Di421Y7Ub/?share_source=copy_web&vd_source=e3fc1949b6a1a4e905513dc3fb1eecad",
            ),
        )
            .isA<SearchType.BV>()
            .isEqualTo(expectedSearchType)
        expectThat(searchType("https://www.bilibili.com/video/BV1Di421Y7Ub/"))
            .isA<SearchType.BV>()
            .isEqualTo(expectedSearchType)
    }

    @Test
    fun searchType_inputText_returnEP() {
        val expectedSearchType = SearchType.EP("779775")
        expect {
            that(searchType("【葬送的芙莉莲：第1话 冒险的结束】 https://www.bilibili.com/bangumi/play/ep779775/?share_source=copy_web"))
                .isA<SearchType.EP>()
                .isEqualTo(expectedSearchType)
            that(searchType("https://www.bilibili.com/bangumi/play/Ep779775/?share_source=copy_web"))
                .isA<SearchType.EP>()
                .isEqualTo(expectedSearchType)
        }
    }
}
