package com.imcys.bilibilias.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.imcys.bilibilias.BILIBILIASApplication
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.AppSettingsSerializer
import com.imcys.bilibilias.download.DownloadExecutor
import com.imcys.bilibilias.download.DownloadManager
import com.imcys.bilibilias.download.FfmpegMerger
import com.imcys.bilibilias.download.FileOutputManager
import com.imcys.bilibilias.download.NamingConventionHandler
import com.imcys.bilibilias.download.NewDownloadManager
import com.imcys.bilibilias.download.SubtitleDownloader
import com.imcys.bilibilias.download.VideoInfoFetcher
import com.imcys.bilibilias.ui.BILIBILIASAppViewModel
import com.imcys.bilibilias.ui.analysis.AnalysisViewModel
import com.imcys.bilibilias.ui.download.DownloadViewModel
import com.imcys.bilibilias.ui.event.playvoucher.PlayVoucherErrorViewModel
import com.imcys.bilibilias.ui.event.requestFrequent.RequestFrequentViewModel
import com.imcys.bilibilias.ui.home.HomeViewModel
import com.imcys.bilibilias.ui.login.CookieLoginViewModel
import com.imcys.bilibilias.ui.login.QRCodeLoginViewModel
import com.imcys.bilibilias.ui.setting.SettingViewModel
import com.imcys.bilibilias.ui.setting.contract.NamingConventionViewModel
import com.imcys.bilibilias.ui.setting.developer.LineConfigViewModel
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetViewModel
import com.imcys.bilibilias.ui.setting.platform.ParsePlatformViewModel
import com.imcys.bilibilias.ui.setting.roam.RoamViewModel
import com.imcys.bilibilias.ui.setting.storage.StorageManagementViewModel
import com.imcys.bilibilias.ui.tools.donate.DonateViewModel
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorViewModel
import com.imcys.bilibilias.ui.tools.parser.WebParserViewModel
import com.imcys.bilibilias.ui.user.UserViewModel
import com.imcys.bilibilias.ui.user.bangumifollow.BangumiFollowViewModel
import com.imcys.bilibilias.ui.user.folder.UserFolderViewModel
import com.imcys.bilibilias.ui.user.history.UserPlayHistoryViewModel
import com.imcys.bilibilias.ui.user.like.LikeVideoViewModel
import com.imcys.bilibilias.ui.user.work.WorkListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module


val appModule = module {
    single { androidContext().assets }
    single { androidContext().contentResolver }
    single<DataStore<AppSettings>> {
        DataStoreFactory.create(AppSettingsSerializer) {
            androidContext().dataStoreFile("app_setting.pb")
        }
    }
    viewModelOf(::HomeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::QRCodeLoginViewModel)
    viewModelOf(::BILIBILIASAppViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::AnalysisViewModel)
    viewModelOf(::DownloadViewModel)
    viewModelOf(::PlayVoucherErrorViewModel)
    viewModelOf(::RoamViewModel)
    viewModelOf(::WorkListViewModel)
    viewModelOf(::BangumiFollowViewModel)
    viewModelOf(::UserFolderViewModel)
    viewModelOf(::LikeVideoViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::LayoutTypesetViewModel)
    viewModelOf(::UserPlayHistoryViewModel)
    viewModelOf(::FrameExtractorViewModel)
    viewModelOf(::CookieLoginViewModel)
    viewModelOf(::DonateViewModel)
    viewModelOf(::StorageManagementViewModel)
    viewModelOf(::NamingConventionViewModel)
    viewModelOf(::RequestFrequentViewModel)
    viewModelOf(::LineConfigViewModel)
    viewModelOf(::WebParserViewModel)
    viewModelOf(::ParsePlatformViewModel)

    single { VideoInfoFetcher(get(), get(), get()) }
    single { FileOutputManager(androidApplication()) }
    single { DownloadExecutor(get(qualifier = named("DownloadHttpClient")), get()) }
    single { FfmpegMerger(androidApplication(),get()) }
    single { NamingConventionHandler(get()) }
    single { SubtitleDownloader(get(), get(), androidApplication()) }

    single {
        DownloadManager(
            context = androidApplication(),
            downloadTaskRepository = get(),
            videoInfoRepository = get(),
            appAPIService = get(),
            json = get(),
            okHttpClient = get(),
            httpClient = get(qualifier = named("DownloadHttpClient")),
            appSettingsRepository = get(),
        )
    }

    single {
        NewDownloadManager(
            context = androidApplication(),
            downloadTaskRepository = get(),
            videoInfoRepository = get(),
            httpClient = get(qualifier = named("DownloadHttpClient")),
            okHttpClient = get(),
            appSettingsRepository = get(),
            videoInfoFetcher = get(),
            fileOutputManager = get(),
            downloadExecutor = get(),
            ffmpegMerger = get(),
            namingConventionHandler = get(),
            subtitleDownloader = get()
        )
    }
}