package com.imcys.bilibilias.core.common.utils

fun <T> List<T>.selected(item: T): Boolean = contains(item)

fun <E> MutableList<E>.addOrRemove(item: E) = if (contains(item)) {
    remove(item)
} else {
    add(item)
}
