package com.imcys.common.utils

import com.imcys.common.utils.InputParsingUtils.SearchType
import com.imcys.common.utils.InputParsingUtils.searchType
import org.junit.Test
import kotlin.test.assertEquals

class InputParsingUtilsTest {

    @Test
    fun `givenText_thenReturnSearchType`() {
        val type = searchType("BV1QN4y1W7o3")
        assertEquals(SearchType.BV("BV1QN4y1W7o3"), type)

        val type2 = searchType("ep1234")
        assertEquals(SearchType.EP("1234"), type2)

        val type3 = searchType("av5465342")
        assertEquals(SearchType.AV("5465342"), type3)
    }
}
