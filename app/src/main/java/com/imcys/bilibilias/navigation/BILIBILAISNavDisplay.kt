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
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.imcys.bilibilias.common.event.analysisHandleChannel
import com.imcys.bilibilias.common.event.playVoucherErrorChannel
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.ui.analysis.AnalysisScreen
import com.imcys.bilibilias.ui.analysis.navigation.AnalysisRoute
import com.imcys.bilibilias.ui.analysis.videocodeing.VideoCodingInfoRoute
import com.imcys.bilibilias.ui.analysis.videocodeing.VideoCodingInfoScreen
import com.imcys.bilibilias.ui.download.DownloadScreen
import com.imcys.bilibilias.ui.download.navigation.DownloadRoute
import com.imcys.bilibilias.ui.event.playvoucher.PlayVoucherErrorPage
import com.imcys.bilibilias.ui.event.playvoucher.navigation.PlayVoucherErrorRoute
import com.imcys.bilibilias.ui.home.HomeScreen
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.login.LoginScreen
import com.imcys.bilibilias.ui.login.QRCodeLoginScreen
import com.imcys.bilibilias.ui.login.navigation.LoginRoute
import com.imcys.bilibilias.ui.login.navigation.QRCodeLoginRoute
import com.imcys.bilibilias.ui.setting.SettingScreen
import com.imcys.bilibilias.ui.setting.complaint.ComplaintRoute
import com.imcys.bilibilias.ui.setting.complaint.ComplaintScreen
import com.imcys.bilibilias.ui.setting.navigation.RoamRoute
import com.imcys.bilibilias.ui.setting.navigation.SettingRoute
import com.imcys.bilibilias.ui.setting.roam.RoamScreen
import com.imcys.bilibilias.ui.user.UserScreen
import com.imcys.bilibilias.ui.user.bangumifollow.BangumiFollowRoute
import com.imcys.bilibilias.ui.user.bangumifollow.BangumiFollowScreen
import com.imcys.bilibilias.ui.user.folder.UserFolderRoute
import com.imcys.bilibilias.ui.user.folder.UserFolderScreen
import com.imcys.bilibilias.ui.user.like.LikePageType
import com.imcys.bilibilias.ui.user.like.LikeVideoRoute
import com.imcys.bilibilias.ui.user.like.LikeVideoScreen
import com.imcys.bilibilias.ui.user.navigation.UserRoute
import com.imcys.bilibilias.ui.user.work.WorkListRoute
import com.imcys.bilibilias.ui.user.work.WorkListScreen

/**
 * BILIBILAIS导航显示组件
 * 计划迁移到Compose Navigation 3.0
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BILIBILAISNavDisplay() {

    val backStack = remember { mutableStateListOf<NavKey>(HomeRoute()) }

    // 监听解析事件
    LaunchedEffect(Unit) {
        analysisHandleChannel.collect {
            backStack.addWithReuse(AnalysisRoute(it.analysisText))
        }
    }

    LaunchedEffect(Unit) {
        playVoucherErrorChannel.collect {
            backStack.removeLastOrNull()
            backStack.addWithReuse(PlayVoucherErrorRoute)
        }
    }


    val popTransitionSpec = remember {
        ContentTransform(
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
    }

    SharedTransitionLayout {
        NavDisplay(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
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
                popTransitionSpec
            },
            predictivePopTransitionSpec = {
                popTransitionSpec
            },
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
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
                            backStack.addWithReuse(DownloadRoute)
                        }
                    )
                }
                entry<LoginRoute> {
                    LoginScreen(
                        onToBack = { backStack.removeLastOrNull() },
                        goToQRCodeLogin = {
                            backStack.addWithReuse(QRCodeLoginRoute())
                        }
                    )
                }
                entry<QRCodeLoginRoute> {
                    QRCodeLoginScreen(
                        it,
                        onToBack = { backStack.removeLastOrNull() },
                        onBackHomePage = {
                            backStack.clear()
                            backStack.add(HomeRoute(isFormLogin = true))
                        }
                    )
                }
                entry<UserRoute> {
                    UserScreen(
                        userRoute = it,
                        onToBack = { backStack.removeLastOrNull() },
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
                        }
                    )
                }
                entry<AnalysisRoute> {
                    AnalysisScreen(
                        it,
                        this@SharedTransitionLayout,
                        LocalNavAnimatedContentScope.current,
                        onToBack = { backStack.removeLastOrNull() },
                        goToUser = { mid ->
                            backStack.addWithReuse(UserRoute(mid = mid, isAnalysisUser = true))
                        },
                        onToVideoCodingInfo = {
                            backStack.addWithReuse(VideoCodingInfoRoute)
                        }
                    )
                }
                entry<DownloadRoute> {
                    DownloadScreen(onToBack = { backStack.removeLastOrNull() })
                }
                entry<SettingRoute> {
                    SettingScreen(
                        onToRoam = {
                            backStack.addWithReuse(RoamRoute)
                        },
                        onToBack = { backStack.removeLastOrNull() },
                        onToComplaint = { backStack.addWithReuse(ComplaintRoute) }
                    )
                }
                entry<RoamRoute> {
                    RoamScreen(
                        onToBack = { backStack.removeLastOrNull() },
                        onGoToQRCodeLogin = {
                            // 前往TV登录
                            backStack.addWithReuse(
                                QRCodeLoginRoute(
                                    defaultLoginPlatform = LoginPlatform.TV,
                                    isFromRoam = true
                                )
                            )
                        }
                    )
                }
                entry<PlayVoucherErrorRoute> {
                    PlayVoucherErrorPage(
                        onBlack = { backStack.removeLastOrNull() }
                    )
                }
                entry<WorkListRoute> {
                    WorkListScreen(
                        workListRoute = it,
                        onToBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<BangumiFollowRoute> {
                    BangumiFollowScreen(
                        bangumiFollowRoute = it,
                        onToBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<UserFolderRoute> {
                    UserFolderScreen(
                        userFolderRoute = it,
                        onToBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<LikeVideoRoute> {
                    LikeVideoScreen(
                        likeVideoRoute = it,
                        onToBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<ComplaintRoute>{
                    ComplaintScreen(
                        onToBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<VideoCodingInfoRoute>{
                    VideoCodingInfoScreen(
                        onToBack = { backStack.removeLastOrNull() }
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
inline fun <reified T : NavKey> SnapshotStateList<NavKey>.addWithReuse(route: T) {
    val existingIndex = indexOfFirst { it::class == T::class }

    if (existingIndex != -1) {
        val existingRoute = get(existingIndex) as T
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