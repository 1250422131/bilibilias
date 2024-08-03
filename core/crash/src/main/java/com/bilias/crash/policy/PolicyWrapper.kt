package com.bilias.crash.policy

import androidx.annotation.CallSuper
import com.bilias.crash.Info
import com.bilias.crash.group.Group

/**
 * 包装一下，便于多种策略自由组合，最里面的策略最先被执行，以此类推，扩展此类请重写report()方法
 */
abstract class PolicyWrapper<I : Info, P : Policy<I>> protected constructor(
    override val group: Group, private val policy: P?
) : Policy<I> {

    @CallSuper
    override fun report(info: I, group: Group) {
        policy?.report(info, policy.group)
    }
}