package com.imcys.bilibilias.core.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToLong

enum class DataUnit(val shortName: String) {
    BYTES("B"),
    KILOBYTES("KB"),
    MEGABYTES("MB"),
    GIGABYTES("GB"),
    TERABYTES("TB"),
    PETABYTES("PB"),
}

/** Bytes per Kilobyte.*/
private const val BYTES_PER_KB: Long = 1024

/** Bytes per Megabyte.*/
private const val BYTES_PER_MB = BYTES_PER_KB * 1024

/** Bytes per Gigabyte.*/
private const val BYTES_PER_GB = BYTES_PER_MB * 1024

/** Bytes per Terabyte.*/
private const val BYTES_PER_TB = BYTES_PER_GB * 1024

/** Bytes per PetaByte.*/
private const val BYTES_PER_PB = BYTES_PER_TB * 1024

internal fun convertDataUnit(value: Long, sourceUnit: DataUnit, targetUnit: DataUnit): Long {
    val valueInBytes = when (sourceUnit) {
        DataUnit.BYTES -> value
        DataUnit.KILOBYTES -> Math.multiplyExact(value, BYTES_PER_KB)
        DataUnit.MEGABYTES -> Math.multiplyExact(value, BYTES_PER_MB)
        DataUnit.GIGABYTES -> Math.multiplyExact(value, BYTES_PER_GB)
        DataUnit.TERABYTES -> Math.multiplyExact(value, BYTES_PER_TB)
        DataUnit.PETABYTES -> Math.multiplyExact(value, BYTES_PER_PB)
    }
    return when (targetUnit) {
        DataUnit.BYTES -> valueInBytes
        DataUnit.KILOBYTES -> valueInBytes / BYTES_PER_KB
        DataUnit.MEGABYTES -> valueInBytes / BYTES_PER_MB
        DataUnit.GIGABYTES -> valueInBytes / BYTES_PER_GB
        DataUnit.TERABYTES -> valueInBytes / BYTES_PER_TB
        DataUnit.PETABYTES -> valueInBytes / BYTES_PER_PB
    }
}

internal fun convertDataUnit(value: Double, sourceUnit: DataUnit, targetUnit: DataUnit): Double {
    val valueInBytes = when (sourceUnit) {
        DataUnit.BYTES -> value
        DataUnit.KILOBYTES -> value * BYTES_PER_KB
        DataUnit.MEGABYTES -> value * BYTES_PER_MB
        DataUnit.GIGABYTES -> value * BYTES_PER_GB
        DataUnit.TERABYTES -> value * BYTES_PER_TB
        DataUnit.PETABYTES -> value * BYTES_PER_PB
    }
    require(!valueInBytes.isNaN()) { "DataUnit value cannot be NaN." }
    return when (targetUnit) {
        DataUnit.BYTES -> valueInBytes
        DataUnit.KILOBYTES -> valueInBytes / BYTES_PER_KB
        DataUnit.MEGABYTES -> valueInBytes / BYTES_PER_MB
        DataUnit.GIGABYTES -> valueInBytes / BYTES_PER_GB
        DataUnit.TERABYTES -> valueInBytes / BYTES_PER_TB
        DataUnit.PETABYTES -> valueInBytes / BYTES_PER_PB
    }
}

@JvmInline
value class DataSize internal constructor(private val rawBytes: Long) : Comparable<DataSize> {

    val inWholeBytes: Long
        get() = toLong(DataUnit.BYTES)
    val inWholeKilobytes: Long
        get() = toLong(DataUnit.KILOBYTES)
    val inWholeMegabytes: Long
        get() = toLong(DataUnit.MEGABYTES)
    val inWholeGigabytes: Long
        get() = toLong(DataUnit.GIGABYTES)
    val inWholeTerabytes: Long
        get() = toLong(DataUnit.TERABYTES)
    val inWholePetabytes: Long
        get() = toLong(DataUnit.PETABYTES)

    fun toDouble(unit: DataUnit): Double = convertDataUnit(rawBytes.toDouble(), DataUnit.BYTES, unit)

    fun toLong(unit: DataUnit): Long = convertDataUnit(rawBytes, DataUnit.BYTES, unit)

    operator fun unaryMinus(): DataSize = DataSize(-this.rawBytes)

    operator fun plus(other: DataSize): DataSize = DataSize(Math.addExact(this.rawBytes, other.rawBytes))

    operator fun minus(other: DataSize): DataSize {
        return this + (-other) // a - b = a + (-b)
    }

    operator fun times(scale: Int): DataSize = DataSize(Math.multiplyExact(this.rawBytes, scale.toLong()))

    operator fun div(scale: Int): DataSize = DataSize(this.rawBytes / scale)

    operator fun times(scale: Double): DataSize = DataSize((this.rawBytes * scale).roundToLong())

    operator fun div(scale: Double): DataSize = DataSize((this.rawBytes / scale).roundToLong())

    override fun compareTo(other: DataSize): Int = this.rawBytes.compareTo(other.rawBytes)

    override fun toString(): String = String.format("%dB", rawBytes)

    fun toString(unit: DataUnit, decimals: Int = 2): String {
        require(decimals >= 0) { "decimals must be not negative, but was $decimals" }
        val number = toDouble(unit)
        if (number.isInfinite()) return number.toString()
        val newDecimals = decimals.coerceAtMost(12)
        return DecimalFormat("0").run {
            if (newDecimals > 0) minimumFractionDigits = newDecimals
            roundingMode = RoundingMode.HALF_UP
            format(number) + unit.shortName
        }
    }

    companion object {
        fun Long.toDataSize(unit: DataUnit): DataSize = DataSize(convertDataUnit(this, unit, DataUnit.BYTES))

        fun Double.toDataSize(unit: DataUnit): DataSize = DataSize(convertDataUnit(this, unit, DataUnit.BYTES).roundToLong())

        inline val Long.bytes get() = this.toDataSize(DataUnit.BYTES)
        inline val Long.kb get() = this.toDataSize(DataUnit.KILOBYTES)
        inline val Long.mb get() = this.toDataSize(DataUnit.MEGABYTES)
        inline val Long.gb get() = this.toDataSize(DataUnit.GIGABYTES)
        inline val Long.tb get() = this.toDataSize(DataUnit.TERABYTES)
        inline val Long.pb get() = this.toDataSize(DataUnit.PETABYTES)

        inline val Int.bytes get() = this.toLong().toDataSize(DataUnit.BYTES)
        inline val Int.kb get() = this.toLong().toDataSize(DataUnit.KILOBYTES)
        inline val Int.mb get() = this.toLong().toDataSize(DataUnit.MEGABYTES)
        inline val Int.gb get() = this.toLong().toDataSize(DataUnit.GIGABYTES)
        inline val Int.tb get() = this.toLong().toDataSize(DataUnit.TERABYTES)
        inline val Int.pb get() = this.toLong().toDataSize(DataUnit.PETABYTES)

        inline val Double.bytes get() = this.toDataSize(DataUnit.BYTES)
        inline val Double.kb get() = this.toDataSize(DataUnit.KILOBYTES)
        inline val Double.mb get() = this.toDataSize(DataUnit.MEGABYTES)
        inline val Double.gb get() = this.toDataSize(DataUnit.GIGABYTES)
        inline val Double.tb get() = this.toDataSize(DataUnit.TERABYTES)
        inline val Double.pb get() = this.toDataSize(DataUnit.PETABYTES)
    }
}
