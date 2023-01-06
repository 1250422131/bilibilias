package com.imcys.bilibilias.common.base.utils


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