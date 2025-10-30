package com.imcys.bilibilias.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.imcys.bilibilias.common.event.analysisHandleChannel
import com.imcys.bilibilias.common.event.playVoucherErrorChannel
import com.imcys.bilibilias.common.event.requestFrequentHandleChannel
import com.imcys.bilibilias.ui.analysis.AnalysisScreen
import com.imcys.bilibilias.ui.analysis.AnalysisViewModel
import com.imcys.bilibilias.ui.analysis.navigation.AnalysisRoute
import com.imcys.bilibilias.ui.analysis.videocodeing.VideoCodingInfoRoute
import com.imcys.bilibilias.ui.analysis.videocodeing.VideoCodingInfoScreen
import com.imcys.bilibilias.ui.download.DownloadScreen
import com.imcys.bilibilias.ui.download.navigation.DownloadRoute
import com.imcys.bilibilias.ui.event.playvoucher.PlayVoucherErrorPage
import com.imcys.bilibilias.ui.event.playvoucher.navigation.PlayVoucherErrorRoute
import com.imcys.bilibilias.ui.event.requestFrequent.RequestFrequentRoute
import com.imcys.bilibilias.ui.event.requestFrequent.RequestFrequentScreen
import com.imcys.bilibilias.ui.home.HomeScreen
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.login.CookeLoginRoute
import com.imcys.bilibilias.ui.login.CookeLoginScreen
import com.imcys.bilibilias.ui.login.LoginScreen
import com.imcys.bilibilias.ui.login.QRCodeLoginScreen
import com.imcys.bilibilias.ui.login.navigation.LoginRoute
import com.imcys.bilibilias.ui.login.navigation.QRCodeLoginRoute
import com.imcys.bilibilias.ui.setting.SettingScreen
import com.imcys.bilibilias.ui.setting.about.AboutRouter
import com.imcys.bilibilias.ui.setting.about.AboutScreen
import com.imcys.bilibilias.ui.setting.complaint.ComplaintRoute
import com.imcys.bilibilias.ui.setting.complaint.ComplaintScreen
import com.imcys.bilibilias.ui.setting.contract.NamingConventionRoute
import com.imcys.bilibilias.ui.setting.contract.NamingConventionScreen
import com.imcys.bilibilias.ui.setting.expand.SystemExpandRoute
import com.imcys.bilibilias.ui.setting.expand.SystemExpandScreen
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetRoute
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetScreen
import com.imcys.bilibilias.ui.setting.navigation.RoamRoute
import com.imcys.bilibilias.ui.setting.navigation.SettingRoute
import com.imcys.bilibilias.ui.setting.roam.RoamScreen
import com.imcys.bilibilias.ui.setting.storage.StorageManagementRoute
import com.imcys.bilibilias.ui.setting.storage.StorageManagementScreen
import com.imcys.bilibilias.ui.setting.version.AppVersionInfoRoute
import com.imcys.bilibilias.ui.setting.version.AppVersionInfoScreen
import com.imcys.bilibilias.ui.tools.donate.DonateRoute
import com.imcys.bilibilias.ui.tools.donate.DonateScreen
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorRoute
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorScreen
import com.imcys.bilibilias.ui.user.UserScreen
import com.imcys.bilibilias.ui.user.bangumifollow.BangumiFollowRoute
import com.imcys.bilibilias.ui.user.bangumifollow.BangumiFollowScreen
import com.imcys.bilibilias.ui.user.folder.UserFolderRoute
import com.imcys.bilibilias.ui.user.folder.UserFolderScreen
import com.imcys.bilibilias.ui.user.history.UserPlayHistoryRoute
import com.imcys.bilibilias.ui.user.history.UserPlayHistoryScreen
import com.imcys.bilibilias.ui.user.like.LikePageType
import com.imcys.bilibilias.ui.user.like.LikeVideoRoute
import com.imcys.bilibilias.ui.user.like.LikeVideoScreen
import com.imcys.bilibilias.ui.user.navigation.UserRoute
import com.imcys.bilibilias.ui.user.work.WorkListRoute
import com.imcys.bilibilias.ui.user.work.WorkListScreen
import org.koin.androidx.compose.koinViewModel

/**
 * BILIBILAIS导航显示组件
 * 计划迁移到Compose Navigation 3.0
 */
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BILIBILAISNavDisplay() {
    val backStack = rememberNavBackStack(HomeRoute())
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    // 监听解析事件
    LaunchedEffect(Unit) {
        analysisHandleChannel.collect {
            backStack.addWithReuse(AnalysisRoute(it.analysisText))
        }
    }

    LaunchedEffect(Unit) {
        playVoucherErrorChannel.collect {
            backStack.removeLastOrNullSafe()
            backStack.addWithReuse(PlayVoucherErrorRoute)
        }
    }

    LaunchedEffect(Unit) {
        requestFrequentHandleChannel.collect {
            backStack.addWithReuse(RequestFrequentRoute(it.url))
        }
    }

    fun createPopTransitionSpec() = ContentTransform(
        // 返回导航：上一个页面进入 - 从放大状态恢复
        scaleIn(
            initialScale = 1.1F,
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        ),
        // 返回导航：当前页面退出 - 淡出+放大
        fadeOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        )
    )

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNullSafe() },
            sceneStrategy = listDetailStrategy,
            transitionSpec = {
                ContentTransform(
                    // 正向导航：新页面进入 - 只是淡入
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    // 正向导航：原页面退出 - 放大并保持可见
                    scaleOut(
                        targetScale = 1.1F,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    )
                )
            },
            popTransitionSpec = {
                createPopTransitionSpec()
            },
            predictivePopTransitionSpec = {
                createPopTransitionSpec()
            },
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    HomeScreen(
                        it,
                        this@SharedTransitionLayout,
                        LocalNavAnimatedContentScope.current,
                        goToLogin = {
                            backStack.addWithReuse(LoginRoute)
                        },
                        goToUserPage = { mid ->
                            backStack.addWithReuse(UserRoute(mid = mid))
                        },
                        goToAnalysis = {
                            backStack.addWithReuse(AnalysisRoute())
                        },
                        goToDownloadPage = {
                            backStack.addWithReuse(DownloadRoute())
                        },
                        goToSetting = {
                            backStack.addWithReuse(SettingRoute)
                        },
                        goToPage = { page ->
                            backStack.addWithReuse(page)
                        }
                    )
                }
                entry<LoginRoute> {
                    LoginScreen(
                        onToBack = { backStack.removeLastOrNullSafe() },
                        goToQRCodeLogin = {
                            backStack.addWithReuse(QRCodeLoginRoute())
                        }
                    )
                }
                entry<QRCodeLoginRoute> {
                    QRCodeLoginScreen(
                        it,
                        onToBack = { backStack.removeLastOrNullSafe() },
                        onBackHomePage = {
                            backStack.clear()
                            backStack.add(HomeRoute(isFormLogin = true))
                        },
                        onToCookieLogin = {
                            backStack.add(CookeLoginRoute)
                        }
                    )
                }
                entry<UserRoute> {
                    UserScreen(
                        userRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() },
                        onToSettings = {
                            backStack.addWithReuse(SettingRoute)
                        },
                        onToWorkList = { mid ->
                            backStack.add(WorkListRoute(mid = mid))
                        },
                        onToBangumiFollow = { mid ->
                            backStack.add(BangumiFollowRoute(mid = mid))
                        },
                        onToUserFolder = { mid ->
                            backStack.add(UserFolderRoute(mid = mid))
                        },
                        onToLikeVideo = { mid ->
                            backStack.add(LikeVideoRoute(mid = mid, type = LikePageType.LIKE))
                        },
                        onToCoinVide = { mid ->
                            backStack.add(LikeVideoRoute(mid = mid, type = LikePageType.COIN))
                        },
                        onToPlayHistory = {
                            backStack.add(UserPlayHistoryRoute)
                        }
                    )
                }
                entry<AnalysisRoute> {
                    val vm = koinViewModel<AnalysisViewModel>(key = it.toString())
                    AnalysisScreen(
                        it,
                        vm,
                        this@SharedTransitionLayout,
                        LocalNavAnimatedContentScope.current,
                        onToBack = { backStack.removeLastOrNullSafe() },
                        goToUser = { mid ->
                            backStack.add(UserRoute(mid = mid, isAnalysisUser = true))
                        },
                        onToVideoCodingInfo = {
                            backStack.addWithReuse(VideoCodingInfoRoute)
                        },
                        onToLogin = {
                            backStack.addWithReuse(QRCodeLoginRoute(isFromAnalysis = true))
                        }
                    )
                }
                entry<DownloadRoute> {
                    DownloadScreen(
                        it,
                        onToBack = { backStack.removeLastOrNullSafe() })
                }
                entry<SettingRoute>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "请选择右侧选项",
                                )
                            }
                        }
                    )
                ) {
                    SettingScreen(
                        onToRoam = {
                            backStack.addWithReuse(RoamRoute)
                        },
                        onToBack = { backStack.removeLastOrNullSafe() },
                        onToComplaint = { backStack.addWithReuse(ComplaintRoute) },
                        onToLayoutTypeset = { backStack.addWithReuse(LayoutTypesetRoute) },
                        onToAbout = { backStack.addWithReuse(AboutRouter) },
                        onToVersionInfo = { backStack.addWithReuse(AppVersionInfoRoute) },
                        onToSystemExpand = { backStack.addWithReuse(SystemExpandRoute) },
                        onToStorageManagement = { backStack.addWithReuse(StorageManagementRoute) },
                        onToNamingConvention = { backStack.addWithReuse(NamingConventionRoute) },
                        onLogoutFinish = {
                            backStack.firstOrNull {
                                it is UserRoute && !it.isAnalysisUser
                            }?.let {
                                backStack.remove(it)
                            }
                        }
                    )
                }
                entry<RoamRoute>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    RoamScreen(
                        onToBack = { backStack.removeLastOrNullSafe() },
                        onGoToQRCodeLogin = {
                            backStack.addWithReuse(
                                QRCodeLoginRoute(
                                    defaultLoginPlatform = it,
                                    isFromRoam = true
                                )
                            )
                        }
                    )
                }
                entry<PlayVoucherErrorRoute> {
                    PlayVoucherErrorPage(
                        onBlack = {
                            backStack.removeLastOrNullSafe()
                            backStack.add(HomeRoute())
                        }
                    )
                }
                entry<WorkListRoute> {
                    WorkListScreen(
                        workListRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<BangumiFollowRoute> {
                    BangumiFollowScreen(
                        bangumiFollowRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<UserFolderRoute> {
                    UserFolderScreen(
                        userFolderRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<LikeVideoRoute> {
                    LikeVideoScreen(
                        likeVideoRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<ComplaintRoute>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    ComplaintScreen(
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<VideoCodingInfoRoute> {
                    VideoCodingInfoScreen(
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<LayoutTypesetRoute>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    LayoutTypesetScreen(
                        layoutTypesetRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<UserPlayHistoryRoute> {
                    UserPlayHistoryScreen(
                        userPlayHistoryRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<AboutRouter>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    AboutScreen(
                        aboutRouter = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<AppVersionInfoRoute>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    AppVersionInfoScreen(
                        appVersionInfoRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<FrameExtractorRoute> {
                    FrameExtractorScreen(
                        frameExtractorRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<CookeLoginRoute> {
                    CookeLoginScreen(cookeLoginRoute = it, onToBack = {
                        backStack.removeLastOrNullSafe()
                    }, onFinish = {
                        backStack.clear()
                        backStack.add(HomeRoute(isFormLogin = true))
                    })
                }
                entry<DonateRoute> {
                    DonateScreen(donateRoute = it, onToBack = {
                        backStack.removeLastOrNullSafe()
                    })
                }
                entry<SystemExpandRoute>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    SystemExpandScreen(systemExpandRoute = it, onToBack = {
                        backStack.removeLastOrNullSafe()
                    })
                }
                entry<StorageManagementRoute>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    StorageManagementScreen(
                        route = it,
                        onToBack = { backStack.removeLastOrNullSafe() },
                        onToDownloadList = {
                            backStack.add(DownloadRoute(1))
                        }
                    )
                }
                entry<NamingConventionRoute> (
                    metadata = ListDetailSceneStrategy.detailPane()
                ) {
                    NamingConventionScreen(
                        namingConventionRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
                entry<RequestFrequentRoute> {
                    RequestFrequentScreen (
                        requestFrequentRoute = it,
                        onToBack = { backStack.removeLastOrNullSafe() }
                    )
                }
            }
        )
    }

}


/**
 * 栈内复用扩展函数
 * 如果栈中已存在相同类型的路由，则比较参数：
 * - 参数相同：将其之后的所有元素移除（目标及之前的保留）
 * - 参数不同：替换该路由实例并移除其之后的所有元素
 * 否则添加新的路由实例
 */
inline fun <reified T : NavKey> NavBackStack<T>.addWithReuse(route: T) {
    val existingIndex = indexOfFirst { it::class == T::class }

    if (existingIndex != -1) {
        val existingRoute = get(existingIndex)
        // 比较路由对象的完整内容，而不只是类型
        if (existingRoute == route) {
            // 参数相同，只需移除目标之后的所有元素
            repeat(size - existingIndex - 1) { removeAt(existingIndex + 1) }
        } else {
            // 参数不同，替换该路由并移除其之后的所有元素
            set(existingIndex, route)
            repeat(size - existingIndex - 1) { removeAt(existingIndex + 1) }
        }
    } else {
        add(route)
    }
}

/**
 * 安全移除栈顶元素扩展函数
 * 只要栈中元素大于1时才允许移除，防止最后一页被移除导致异常
 */
fun <T : NavKey> NavBackStack<T>.removeLastOrNullSafe() {
    if (this.size > 1) {
        this.removeLastOrNull()
    }
}
