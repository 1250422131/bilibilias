package com.imcys.bilias.feature.merge.ass.model

fun buildTime(start: Long, end: Long) {

}

data class Time(private val start: Long, private val end: Long) {
    private fun conversion(seconds: Long): String {
        val second = seconds % 60
        val minute = (seconds / 60) % 60
        val hour = seconds / (60 * 60)
        return "%01d:%02d:%02d".format(hour, minute, second)
    }
}
