package com.imcys.bilibilias.di

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.imcys.bilibilias.core.navigation.AsBackStack
import com.imcys.bilibilias.core.navigation.AsBackStackViewModel
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.navigation.CacheRoute
import com.imcys.bilibilias.navigation.LoginRoute
import com.imcys.bilibilias.navigation.PolymorphicModuleBuilders
import com.imcys.bilibilias.navigation.SearchRoute
import com.imcys.bilibilias.navigation.SettingRoute
import com.imcys.bilibilias.navigation.TopLevelDestination
import com.imcys.bilibilias.ui.cache.CacheScreen
import com.imcys.bilibilias.ui.login.LoginScreen
import com.imcys.bilibilias.ui.search.SearchScreen
import com.imcys.bilibilias.ui.setting.SettingsScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val NavigationModule = module {
    viewModelOf(::AsBackStackViewModel)
    single { AsBackStack(TopLevelDestination.SEARCH.key) }
    single {
        SerializersModule {
            polymorphic(AsNavKey::class) {
                PolymorphicModuleBuilders.forEach { it }
            }
        }
    }
    single<EntryProviderBuilder<AsNavKey>.() -> Unit> {
        {
            val backStack: AsBackStack = get()
            entry<SearchRoute> {
                SearchScreen(
                    navigationToLogin = { backStack.navigate(LoginRoute) },
                    navigationToPlayer = {},
                    navigationToSettings = { backStack.navigate(SettingRoute) }
                )
            }
            entry<CacheRoute> {
                CacheScreen()
            }
            entry<LoginRoute> {
                LoginScreen(
                    onShowSnackbar = { _, _ -> true },
                    onBack = { backStack.popLast() }
                )
            }
            entry<SettingRoute> {
                SettingsScreen(
                    onBack = { backStack.popLast() }
                )
            }
        }
    }
}