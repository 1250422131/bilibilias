package com.imcys.bilibilias.di

import com.imcys.bilibilias.BILIBILIASApplication
import com.imcys.bilibilias.datastore.userAppSettingsStore
import com.imcys.bilibilias.dwonload.DownloadManager
import com.imcys.bilibilias.ui.BILIBILIASAppViewModel
import com.imcys.bilibilias.ui.analysis.AnalysisViewModel
import com.imcys.bilibilias.ui.download.DownloadViewModel
import com.imcys.bilibilias.ui.event.playvoucher.PlayVoucherErrorViewModel
import com.imcys.bilibilias.ui.home.HomeViewModel
import com.imcys.bilibilias.ui.login.CookieLoginViewModel
import com.imcys.bilibilias.ui.login.QRCodeLoginViewModel
import com.imcys.bilibilias.ui.setting.SettingViewModel
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetViewModel
import com.imcys.bilibilias.ui.setting.roam.RoamViewModel
import com.imcys.bilibilias.ui.setting.storage.StorageManagementViewModel
import com.imcys.bilibilias.ui.tools.donate.DonateViewModel
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorViewModel
import com.imcys.bilibilias.ui.user.UserViewModel
import com.imcys.bilibilias.ui.user.bangumifollow.BangumiFollowViewModel
import com.imcys.bilibilias.ui.user.folder.UserFolderViewModel
import com.imcys.bilibilias.ui.user.history.UserPlayHistoryViewModel
import com.imcys.bilibilias.ui.user.like.LikeVideoViewModel
import com.imcys.bilibilias.ui.user.work.WorkListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val appModule = module {
    single { androidContext().assets }
    single { androidContext().contentResolver }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { QRCodeLoginViewModel(get(), get(), get(), get()) }
    viewModel { BILIBILIASAppViewModel(get(), get(), get(), get(), get()) }
    viewModel { UserViewModel(get()) }
    viewModel { AnalysisViewModel(get(), get(), get(), get(), get()) }
    viewModel { DownloadViewModel(get(), get(), get()) }
    viewModel { PlayVoucherErrorViewModel(get()) }
    viewModel { RoamViewModel(get(), get(), get(), get()) }
    viewModel { WorkListViewModel(get()) }
    viewModel { BangumiFollowViewModel(get()) }
    viewModel { UserFolderViewModel(get()) }
    viewModel { LikeVideoViewModel(get()) }
    viewModel { SettingViewModel(get(),get(),get(),get(),get(),get()) }
    viewModel { LayoutTypesetViewModel(get()) }
    viewModel { UserPlayHistoryViewModel(get()) }
    viewModel { FrameExtractorViewModel(get(), get(), get()) }
    viewModel { CookieLoginViewModel(get(), get(), get(),get()) }
    viewModel { DonateViewModel(get()) }
    viewModel { StorageManagementViewModel(get()) }
    factory { androidContext().userAppSettingsStore }
    single {
        DownloadManager(
            androidContext() as BILIBILIASApplication,
            get(),
            get(),
            get(),
            get(),
            get(qualifier = named("DownloadHttpClient"))
        )
    }
}