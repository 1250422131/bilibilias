package com.imcys.datastore.fastkv

interface ICookieStore {
    val valid: Boolean
    fun setTime(timestamp: Long)
    fun get(): ByteArray?
    fun set(byteArray: ByteArray)
    fun clear()
}