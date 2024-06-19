package com.imcys.bilibilias.core.database

interface DatabaseObservable {
    //register the observer with this method
    fun registerDbObserver(databaseObserver: DatabaseObserver)

    //unregister the observer with this method
    fun removeDbObserver(databaseObserver: DatabaseObserver)

    //call this method upon database change
    fun notifyDbChanged()
}