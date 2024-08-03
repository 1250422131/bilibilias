package com.imcys.bilibilias.okdownloader.android

import java.util.concurrent.CopyOnWriteArraySet

fun interface Observer<T> {

    fun onChanged(data: T)
}

interface Observable<T> {

    fun notifyObservers(data: T)

    fun notifyObservers() {}

    fun observe(observer: Observer<T>)

    fun unObserve(observer: Observer<T>)
}

abstract class BaseObservable<T> : Observable<T> {

    private val mObservers = CopyOnWriteArraySet<Observer<T>>()

    override fun notifyObservers(data: T) {
        mObservers.forEach { it.onChanged(data) }
    }

    override fun observe(observer: Observer<T>) {
        mObservers.add(observer)
    }

    override fun unObserve(observer: Observer<T>) {
        mObservers.remove(observer)
    }
}
