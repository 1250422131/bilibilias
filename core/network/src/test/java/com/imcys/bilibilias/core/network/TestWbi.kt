package com.imcys.bilibilias.core.network

import com.imcys.bilibilias.core.network.utils.TokenUtil
import com.imcys.bilibilias.core.network.utils.WBIUtils
import org.junit.Test
import kotlin.test.assertEquals

class TestWbi {
    private val imgKey =
        "https://i0.hdslb.com/bfs/wbi/7cd084941338484aae1ad9425b84077c.png".replace(".png", "")
            .split('/').last()
    private val subKey =
        "https://i0.hdslb.com/bfs/wbi/4932caff0ff746eab6f01bf08b70ac45.png".replace(".png", "")
            .split('/').last()
    private val mix = imgKey + subKey

    @Test
    fun `test wbi1`() {
        assertEquals("7cd084941338484aae1ad9425b84077c", imgKey)
        assertEquals("4932caff0ff746eab6f01bf08b70ac45", subKey)
        assertEquals("ea1db124af3c7062474693fa704f4ff8", WBIUtils.getMixinKey(imgKey, subKey))
    }

    @Test
    fun `test wbi2`() {
        val mixinKey = WBIUtils.getMixinKey(imgKey, subKey)
        val list = listOf(
            Parameter("foo", "114"),
            Parameter("bar", "514"),
            Parameter("zab", "1919810"),
            Parameter("wts", "1702204169")
        )
        val s = WBIUtils.encWbi(list, mixinKey)
        println(s.joinToString("&") { it.name + '=' + it.value })
    }

    @Test
    fun `测试 TokenUtils`() {
        val map = mutableMapOf(
            "foo" to "114",
            "bar" to "514",
            "zab" to "1919810",
            "wts" to "1702204169"
        )
        TokenUtil.genBiliSign(map, mix)
    }
}
