package com.imcys.bilibilias.logic

import com.imcys.bilibilias.logic.login.CookieStateMachine
import com.imcys.bilibilias.logic.login.LoginStateMachine
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val LogicModule = module {
    factoryOf(::LoginStateMachine)
    factoryOf(::CookieStateMachine)
}