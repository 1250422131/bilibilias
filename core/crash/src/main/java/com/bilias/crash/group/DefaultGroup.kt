package com.bilias.crash.group

class DefaultGroup : Group {
    override val name: String = this.javaClass.simpleName
    override fun counts(): Boolean = true
}