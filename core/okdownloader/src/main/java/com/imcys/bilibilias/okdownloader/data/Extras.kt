package com.imcys.bilibilias.okdownloader.data

open class Extras(protected val data: Map<String, String>) {
    fun getString(key: String, defaultValue: String): String {
        return data[key] ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return data[key]?.toInt() ?: defaultValue
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return data[key]?.toLong() ?: defaultValue
    }

    fun getDouble(key: String, defaultValue: Double): Double {
        return data[key]?.toDouble() ?: defaultValue
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return data[key]?.toFloat() ?: defaultValue
    }

    fun toMutableExtras(): MutableExtras {
        return MutableExtras(data.toMutableMap())
    }

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return data.isNotEmpty()
    }

    val map: Map<String, String>
        get() {
            return data.toMap()
        }

    override fun toString(): String {
        return data.toString()
    }

    open fun copy(): Extras {
        return Extras(data.toMap())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Extras

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    companion object {
        val emptyExtras = Extras(emptyMap())
    }
}
