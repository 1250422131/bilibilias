package com.imcys.bilibilias.logic.di

import com.imcys.bilibilias.logic.cache.CacheComponent
import com.imcys.bilibilias.logic.cache.DefaultCacheComponent
import com.imcys.bilibilias.logic.login.DefaultLoginComponent
import com.imcys.bilibilias.logic.login.LoginComponent
import com.imcys.bilibilias.logic.player.DefaultPlayerComponent
import com.imcys.bilibilias.logic.player.PlayerComponent
import com.imcys.bilibilias.logic.root.DefaultRootComponent
import com.imcys.bilibilias.logic.root.RootComponent
import com.imcys.bilibilias.logic.search.DefaultSearchComponent
import com.imcys.bilibilias.logic.search.SearchComponent
import com.imcys.bilibilias.logic.setting.DefaultSettingsComponent
import com.imcys.bilibilias.logic.setting.SettingsComponent
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val logicModule = module {
    singleOf(::DefaultRootComponent) bind RootComponent::class
    singleOf(::DefaultSearchComponent) bind SearchComponent::class
    singleOf(::DefaultCacheComponent) bind CacheComponent::class
    singleOf(::DefaultLoginComponent) bind LoginComponent::class
    singleOf(::DefaultPlayerComponent) bind PlayerComponent::class
    singleOf(::DefaultSettingsComponent) bind SettingsComponent::class
}