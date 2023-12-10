package com.bilias.crash

import com.bilias.crash.policy.Policy

abstract class Collector<I : Info, P : Policy<*>> {
    /**
     * 收集崩溃信息，仅仅将CrashInfo缓存在Collector
     */
    abstract fun collect(info: I)

    /**
     * 根据需要将收集到的崩溃信息反馈给开发者
     */
    abstract fun report(policy: P)
}