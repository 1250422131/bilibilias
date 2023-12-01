package com.imcys.network.repository

data class Parameter(val first: String, val second: String)
interface WebInterface {
    fun buildParameter(): List<Parameter>
}
