package com.imcys.bilibilias.core.utils

import kotlin.reflect.KProperty

fun <K, V> Map<K, V?>.print(space: Int = 0): String {
    // 生成当前层次的行缩进，用space个空格表示，当前层次每一行内容都需要带上缩进
    val indent = buildString { repeat(space) { append(" ") } }
    val sb = StringBuilder("\n$indent{")
    this.iterator().forEach { entry ->
        // 如果值是 Map 类型，则递归调用print()生成其结构化键值对子串，否则返回值本身
        val value = entry.value.let { v ->
            (v as? Map<*, *>)?.print("${indent}${entry.key} = ".length) ?: v.toString()
        }
        sb.append("\n\t$indent[${entry.key}] = $value,")
    }
    sb.append("\n$indent}")
    return sb.toString()
}

fun Any.ofMap(): Map<String, Any?>? {
    return this::class.takeIf { it.isData }
        ?.members?.filterIsInstance<KProperty<Any?>>()?.associate { member ->
            val value = member.call(this)?.let { v ->
                // 若成员变量是data class，则递归调用ofMap()，将其转化成键值对，否则直接返回值
                if (v::class.isData) v.ofMap() else v
            }
            member.name to value
        }
}
