package com.imcys.bilibilias.logic

import com.imcys.bilibilias.logic.login.CookieStateMachine
import com.imcys.bilibilias.logic.login.QrCodeLoginStateMachine
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val LogicModule = module {
    factoryOf(::CookieStateMachine)
    factoryOf(::QrCodeLoginStateMachine)
}