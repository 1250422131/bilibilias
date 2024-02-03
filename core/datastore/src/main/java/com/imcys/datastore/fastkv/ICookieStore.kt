package com.imcys.datastore.fastkv

interface ICookieStore {
    val valid: Boolean
    var refreshToken: String
    fun setTime(timestamp: Long)
    fun get(): ByteArray?
    fun set(byteArray: ByteArray)
    fun clear()
}
