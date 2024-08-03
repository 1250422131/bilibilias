package com.imcys.bilias.feature.merge.move

interface IMoveWorker {
    fun enqueue(path: String, viewTitle: String)
    fun execute(callback: (String) -> Unit)
    fun delete(path: String)
}
