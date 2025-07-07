package com.imcys.bilibilias.common.utils

import java.math.BigDecimal
import java.math.RoundingMode

object NumberUtils {

    private const val WAN = 10000.0
    private const val YI = 100000000.0

    /**
     * Formats a large number into a more readable string with suffixes like '万' or '亿'.
     *
     * @param number The number to format.
     * @return A formatted string. For example, 12345 becomes "1.2万", 123456789 becomes "1.2亿".
     */
    fun formatLargeNumber(number: Long?): String {
        if (number == null) return "0"
        if (number < WAN) {
            return number.toString()
        }

        return if (number < YI) {
            formatValue(number / WAN, "万")
        } else {
            formatValue(number / YI, "亿")
        }
    }

    /**
     * Helper function to format the value to one decimal place if it's not a whole number.
     */
    private fun formatValue(value: Double, unit: String): String {
        val bigDecimal = BigDecimal(value).setScale(1, RoundingMode.DOWN)
        // If the value is a whole number after scaling, display it as an integer.
        // Otherwise, display it with one decimal place.
        return if (bigDecimal.scale() == 0 || bigDecimal.stripTrailingZeros().scale() <= 0) {
            "${bigDecimal.longValueExact()}$unit"
        } else {
            "${bigDecimal.toPlainString()}$unit"
        }
    }
}
