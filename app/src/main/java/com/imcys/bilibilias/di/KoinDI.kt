package com.imcys.bilibilias.di

import com.imcys.bilibilias.ui.BILIBILIASAppViewModel
import com.imcys.bilibilias.ui.home.HomeViewModel
import com.imcys.bilibilias.ui.login.QRCodeLoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { androidContext().assets }
    single { androidContext().contentResolver }
    viewModel { HomeViewModel(get(),get(),get()) }
    viewModel { QRCodeLoginViewModel(get(),get(),get(),get()) }

    viewModel { BILIBILIASAppViewModel(get()) }
}