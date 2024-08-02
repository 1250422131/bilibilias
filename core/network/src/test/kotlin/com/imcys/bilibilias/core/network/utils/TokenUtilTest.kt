package com.imcys.bilibilias.core.network.utils

import com.imcys.bilibilias.core.model.login.NavigationBar
import com.imcys.bilibilias.core.network.Parameter
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TokenUtilTest {
    private val nav = NavigationBar(
        wbiImg = NavigationBar.WbiImg(
            imgUrl = "https://i0.hdslb.com/bfs/wbi/7cd084941338484aae1ad9425b84077c.png",
            subUrl = "https://i0.hdslb.com/bfs/wbi/4932caff0ff746eab6f01bf08b70ac45.png",
        ),
    )

    @BeforeTest
    fun before() {
        TokenUtil.setCacheToken(TokenUtil.getMixinKey(nav.imgKey, nav.subKey))
    }

    @Test
    fun inputImgKeyAndSubKey_returnMixKey() {
        assertEquals(
            "ea1db124af3c7062474693fa704f4ff8",
            TokenUtil.getMixinKey(nav.imgKey, nav.subKey),
        )
    }

    @Test
    fun dd() {
        assertEquals(
            "bar=514&foo=114&zab=1919810&w_rid=8f6f2b5b3d485fe1886cec6a0be8c5d4&wts=1702204169",
            encodeURLParameter(
                TokenUtil.encWbi(
                    mapOf("foo" to "114", "bar" to "514", "zab" to "1919810"),
                    1702204169 * 1000,
                ),
            ),
        )
    }

    private fun encodeURLParameter(params: List<Parameter>): String {
        return params.joinToString("&") {
            "${it.name}=${it.value}"
        }
    }
}