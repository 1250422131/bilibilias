package com.imcys.datastore.fastkv

import android.content.Context
import android.content.SharedPreferences
import io.fastkv.FastKV
import io.fastkv.interfaces.FastEncoder
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class KVData {
    abstract val kv: FastKV
    protected open fun encoders(): Array<FastEncoder<*>>? {
        return null
    }

    protected fun buildKV(
        context: Context,
        name: String,
        block: FastKV.Builder.() -> Unit = {}
    ): FastKV =
        FastKV.Builder(context, name)
            .apply { block(); encoder(encoders()) }.build()

    protected fun <T> stringEnum(key: String, converter: StringEnumConverter<T>) =
        StringEnumProperty(key, converter)

    protected fun <T> intEnum(key: String, converter: IntEnumConverter<T>) =
        IntEnumProperty(key, converter)

    protected fun combineKey(key: String) = CombineKeyProperty(key)

    protected fun string2String(key: String) = StringToStringProperty(key)

    protected fun string2Int(key: String) = StringToIntProperty(key)

    protected fun string2Boolean(key: String) = StringToBooleanProperty(key)

    protected fun int2Boolean(key: String) = IntToBooleanProperty(key)

    class StringEnumProperty<T>(
        private val key: String,
        private val converter: StringEnumConverter<T>
    ) :
        ReadWriteProperty<KVData, T> {
        override fun getValue(thisRef: KVData, property: KProperty<*>): T {
            return converter.stringToType(thisRef.kv.getString(key))
        }

        override fun setValue(thisRef: KVData, property: KProperty<*>, value: T) {
            thisRef.kv.putString(key, converter.typeToString(value))
        }
    }

    class IntEnumProperty<T>(
        private val key: String,
        private val converter: IntEnumConverter<T>
    ) :
        ReadWriteProperty<KVData, T> {
        override fun getValue(thisRef: KVData, property: KProperty<*>): T {
            return converter.intToType(thisRef.kv.getInt(key))
        }

        override fun setValue(thisRef: KVData, property: KProperty<*>, value: T) {
            thisRef.kv.putInt(key, converter.typeToInt(value))
        }
    }

    inner class CombineKeyProperty(preKey: String) : ReadOnlyProperty<KVData, CombineKV> {
        private var combineKV = CombineKV(preKey)
        override fun getValue(thisRef: KVData, property: KProperty<*>): CombineKV {
            return combineKV
        }
    }

    inner class CombineKV(private val preKey: String) {
        private fun combineKey(key: String): String {
            return preKey + KEY_SEPARATOR + key
        }

        fun containsKey(key: String): Boolean {
            return kv.contains(combineKey(key))
        }

        fun remove(key: String) {
            kv.remove(combineKey(key))
        }

        fun putBoolean(key: String, value: Boolean) {
            kv.putBoolean(combineKey(key), value)
        }

        fun getBoolean(key: String, defValue: Boolean = false): Boolean {
            return kv.getBoolean(combineKey(key), defValue)
        }

        fun putInt(key: String, value: Int) {
            kv.putInt(combineKey(key), value)
        }

        fun getInt(key: String, defValue: Int = 0): Int {
            return kv.getInt(combineKey(key), defValue)
        }

        fun putFloat(key: String, value: Float) {
            kv.putFloat(combineKey(key), value)
        }

        fun getFloat(key: String, defValue: Float = 0f): Float {
            return kv.getFloat(combineKey(key), defValue)
        }

        fun putLong(key: String, value: Long) {
            kv.putLong(combineKey(key), value)
        }

        fun getLong(key: String, defValue: Long = 0L): Long {
            return kv.getLong(combineKey(key), defValue)
        }

        fun putDouble(key: String, value: Double) {
            kv.putDouble(combineKey(key), value)
        }

        fun getDouble(key: String, defValue: Double = 0.0): Double {
            return kv.getDouble(combineKey(key), defValue)
        }

        fun putString(key: String, value: String?) {
            kv.putString(combineKey(key), value)
        }

        fun getString(key: String, defValue: String? = null): String? {
            return kv.getString(combineKey(key), null) ?: defValue
        }

        fun putArray(key: String, value: ByteArray?) {
            kv.putArray(combineKey(key), value)
        }

        fun getArray(key: String, defValue: ByteArray? = null): ByteArray? {
            return kv.getArray(combineKey(key), defValue)
        }

        fun <T> putObject(key: String, value: T, encoder: FastEncoder<T>) {
            kv.putObject(combineKey(key), value, encoder)
        }

        fun <T> getObject(key: String): T? {
            return kv.getObject(combineKey(key))
        }
    }

    inner class StringToStringProperty(preKey: String) : ReadOnlyProperty<KVData, String2String> {
        private val mapper = String2String(CombineKV(preKey))
        override fun getValue(thisRef: KVData, property: KProperty<*>): String2String {
            return mapper
        }
    }

    inner class StringToIntProperty(preKey: String) : ReadOnlyProperty<KVData, String2Int> {
        private val mapper = String2Int(CombineKV(preKey))
        override fun getValue(thisRef: KVData, property: KProperty<*>): String2Int {
            return mapper
        }
    }

    inner class StringToBooleanProperty(preKey: String) : ReadOnlyProperty<KVData, String2Boolean> {
        private val mapper = String2Boolean(CombineKV(preKey))
        override fun getValue(thisRef: KVData, property: KProperty<*>): String2Boolean {
            return mapper
        }
    }

    inner class IntToBooleanProperty(preKey: String) : ReadOnlyProperty<KVData, Int2Boolean> {
        private val mapper = Int2Boolean(CombineKV(preKey))
        override fun getValue(thisRef: KVData, property: KProperty<*>): Int2Boolean {
            return mapper
        }
    }

    companion object {
        val EMPTY_ARRAY = ByteArray(0)
        private const val KEY_SEPARATOR = "-&-"
    }
}

open class KVMapper(protected val combineKV: KVData.CombineKV) {
    fun contains(key: String): Boolean {
        return combineKV.containsKey(key)
    }

    fun remove(key: String) {
        combineKV.remove(key)
    }
}

class String2String(combineKV: KVData.CombineKV) : KVMapper(combineKV) {
    operator fun get(key: String): String? {
        return combineKV.getString(key, null)
    }

    operator fun set(key: String, value: String?) {
        combineKV.putString(key, value)
    }
}

class String2Int(combineKV: KVData.CombineKV) : KVMapper(combineKV) {
    operator fun get(key: String): Int {
        return combineKV.getInt(key)
    }

    operator fun set(key: String, value: Int) {
        combineKV.putInt(key, value)
    }
}

class String2Boolean(combineKV: KVData.CombineKV) : KVMapper(combineKV) {
    operator fun get(key: String): Boolean {
        return combineKV.getBoolean(key)
    }

    operator fun set(key: String, value: Boolean) {
        combineKV.putBoolean(key, value)
    }
}

class Int2Boolean(combineKV: KVData.CombineKV) : KVMapper(combineKV) {
    operator fun get(key: Int): Boolean {
        return combineKV.getBoolean(key.toString())
    }

    operator fun set(key: Int, value: Boolean) {
        combineKV.putBoolean(key.toString(), value)
    }
}

interface StringEnumConverter<T> {
    fun stringToType(str: String?): T
    fun typeToString(type: T): String
}

interface IntEnumConverter<T> {
    fun intToType(value: Int): T
    fun typeToInt(type: T): Int
}

interface IFastKVOwner {
    val name: String
    val kv: FastKV
    fun clearAllKV(): SharedPreferences.Editor = kv.clear()
}

open class FastKVOwner(override val name: String, context: Context) : IFastKVOwner {
    override val kv: FastKV by lazy {
        FastKV.Builder(context, name).build()
    }
}

class FastKVProperty<V>(
    private val get: (String) -> V,
    private val put: Pair<String, V>.() -> Unit
) : ReadWriteProperty<IFastKVOwner, V> {
    override fun getValue(thisRef: IFastKVOwner, property: KProperty<*>): V =
        get(property.name)

    override fun setValue(thisRef: IFastKVOwner, property: KProperty<*>, value: V) {
        put((property.name) to value)
    }
}

fun FastKVOwner.int(defValue: Int = 0) =
    FastKVProperty({ kv.getInt(it, defValue) }, { kv.putInt(first, second) })

fun FastKVOwner.boolean(defValue: Boolean = false) =
    FastKVProperty({ kv.getBoolean(it, defValue) }, { kv.putBoolean(first, second) })

fun FastKVOwner.float(defValue: Float = 0f) =
    FastKVProperty({ kv.getFloat(it, defValue) }, { kv.putFloat(first, second) })

fun FastKVOwner.long(defValue: Long = 0L) =
    FastKVProperty({ kv.getLong(it, defValue) }, { kv.putLong(first, second) })

fun FastKVOwner.double(defValue: Double = 0.0) =
    FastKVProperty({ kv.getDouble(it, defValue) }, { kv.putDouble(first, second) })

fun FastKVOwner.string(defValue: String = ""): FastKVProperty<String> =
    FastKVProperty({ kv.getString(it) ?: defValue }, { kv.putString(first, second) })

fun FastKVOwner.string(): FastKVProperty<String?> =
    FastKVProperty({ kv.getString(it) }, { kv.putString(first, second) })

fun FastKVOwner.array(defValue: ByteArray = KVData.EMPTY_ARRAY): FastKVProperty<ByteArray> =
    FastKVProperty({ kv.getArray(it, defValue) }, { kv.putArray(first, second) })

fun FastKVOwner.array(): FastKVProperty<ByteArray?> =
    FastKVProperty({ kv.getArray(it) }, { kv.putArray(first, second) })

fun FastKVOwner.stringSet(defValue: Set<String>? = null) =
    FastKVProperty({ kv.getStringSet(it, defValue) }, { kv.putStringSet(first, second) })

fun <T> FastKVOwner.obj(encoder: FastEncoder<T>) =
    FastKVProperty({ kv.getObject(it) }, { kv.putObject(first, second, encoder) })