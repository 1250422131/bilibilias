package com.imcys.common.appinitializer

interface AppInitializers {
    /**
     * 初始化代码
     */
    fun init()

    /**
     * @return 初始化类型
     */
    fun getStartType(): AppInitializerStartType

    /**
     * TYPE_SERIES 类型时，权重越大，越先执行
     */
    fun widget(): Int
}
