package com.imcys.bilibilias.okdownloader.data

class MutableExtras(protected val mutableData: MutableMap<String, String> = mutableMapOf()) :
    Extras(mutableData) {
    fun putInt(key: String, value: Int) {
        mutableData[key] = value.toString()
    }

    fun putString(key: String, value: String) {
        mutableData[key] = value
    }

    fun putLong(key: String, value: Long) {
        mutableData[key] = value.toString()
    }

    fun putDouble(key: String, value: Double) {
        mutableData[key] = value.toString()
    }

    fun putFloat(key: String, value: Float) {
        mutableData[key] = value.toString()
    }

    fun putBoolean(key: String, value: Boolean) {
        mutableData[key] = value.toString()
    }

    fun clear() {
        mutableData.clear()
    }

    fun toExtras(): Extras {
        return Extras(mutableData.toMap())
    }

    override fun toString(): String {
        return data.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MutableExtras

        if (mutableData != other.mutableData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + mutableData.hashCode()
        return result
    }
}
