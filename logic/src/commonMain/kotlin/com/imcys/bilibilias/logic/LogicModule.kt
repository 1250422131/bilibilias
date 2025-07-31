package com.imcys.bilibilias.logic

import com.imcys.bilibilias.logic.login.LoginStateMachine
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val LogicModule = module {
    singleOf(::LoginStateMachine)
}