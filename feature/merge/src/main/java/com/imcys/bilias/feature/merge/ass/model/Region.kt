package com.imcys.bilias.feature.merge.ass.model

data class Region(
    val marginL: Int = 20,
    val marginR: Int = 20,
    val marginV: Int = 20,
){
    override fun toString(): String {
        return "$marginL, $marginR, $marginV"
    }
}