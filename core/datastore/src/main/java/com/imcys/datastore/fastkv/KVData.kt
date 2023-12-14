package com.imcys.datastore.fastkv

import android.content.Context
import android.content.SharedPreferences
import io.fastkv.FastKV
import io.fastkv.interfaces.FastEncoder
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

fun FastKVOwner.array(defValue: ByteArray = byteArrayOf()): FastKVProperty<ByteArray> =
    FastKVProperty({ kv.getArray(it, defValue) }, { kv.putArray(first, second) })

fun FastKVOwner.array(): FastKVProperty<ByteArray?> =
    FastKVProperty({ kv.getArray(it) }, { kv.putArray(first, second) })

fun FastKVOwner.stringSet(defValue: Set<String>? = null) =
    FastKVProperty({ kv.getStringSet(it, defValue) }, { kv.putStringSet(first, second) })

fun <T> FastKVOwner.obj(encoder: FastEncoder<T>) =
    FastKVProperty({ kv.getObject(it) }, { kv.putObject(first, second, encoder) })