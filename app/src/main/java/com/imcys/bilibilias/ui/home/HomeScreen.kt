package com.imcys.bilibilias.ui.home

import ClipboardAutoHandler
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.data.ASBuildType
import com.imcys.bilibilias.common.data.getASBuildType
import com.imcys.bilibilias.common.utils.AppUtils.getVersion
import com.imcys.bilibilias.common.utils.openLink
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.download.AppDownloadTask
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.app.AppUpdateConfigInfo
import com.imcys.bilibilias.network.model.app.BulletinConfigInfo
import com.imcys.bilibilias.ui.analysis.navigation.AnalysisRoute
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.utils.rememberHeightSizeClass
import com.imcys.bilibilias.ui.utils.rememberWidthSizeClass
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASCardTextField
import com.imcys.bilibilias.ui.weight.ASHorizontalCenteredHeroCarousel
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.ASLoginPlatformFilterChipRow
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.DownloadTaskCard
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.security.MessageDigest
import kotlin.math.min

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
internal fun HomeScreen(
    homeRoute: HomeRoute,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    goToLogin: () -> Unit,
    goToUserPage: (mid: Long) -> Unit,
    goToAnalysis: () -> Unit,
    goToDownloadPage: () -> Unit,
    goToSetting: () -> Unit = {},
    goToPage: (NavKey) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val vm = koinViewModel<HomeViewModel>()
    val uiState by vm.uiState.collectAsState()
    val loginUserInfoState by vm.loginUserInfoState.collectAsState()
    val userLoginPlatformList by vm.userLoginPlatformList.collectAsState()
    var popupUserInfoState by remember { mutableStateOf(false) }
    val windowHeightSizeClass = rememberHeightSizeClass()
    val downloadListState by vm.downloadListState.collectAsState()
    val appSettings by vm.appSettings.collectAsState(initial = AppSettings.getDefaultInstance())
    val windowsWidthSizeClass = rememberWidthSizeClass()


    ClipboardAutoHandler(
        appSettings = appSettings,
        shouldHandleClipboard = {
            appSettings.agreePrivacyPolicy != AppSettings.AgreePrivacyPolicyState.Default
        },
        onClipboardText = { text ->
            goToPage(AnalysisRoute(asInputText = text))
        },
    )


    LaunchedEffect(homeRoute.isFormLogin) {
        if (homeRoute.isFormLogin && !uiState.fromLoginEventConsumed) {
            vm.onNavigatedFromLogin()
            popupUserInfoState = true
        }
    }

    val homeLayoutTypesetList by vm.homeLayoutTypesetList.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val pagerScope = rememberCoroutineScope()
    BackHandler(enabled = pagerState.currentPage > 0) {
        pagerScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    HomeScaffold(
        snackbarHostState = snackbarHostState,
        loginUserInfoState,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        goToLogin = goToLogin,
        goToUserPage = { goToUserPage.invoke(loginUserInfoState.data?.mid ?: 0L) },
        goToAnalysis = goToAnalysis,
        goToSetting = goToSetting
    ) { p ->

        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 内容区
            HorizontalPager(pagerState, modifier = Modifier.weight(1f)) { page ->
                Column(Modifier.fillMaxSize()) {
                    when (page) {
                        0 -> {
                            HomeContent(
                                vm,
                                homeLayoutTypesetList,
                                downloadListState,
                                goToDownloadPage,
                                goToPage
                            )
                        }

                        1 -> {
                            ToolsScreen(vm, goToPage)
                        }
                    }
                }
            }

            // 分页指示器
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(2) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        Modifier
                            .padding(2.dp)
                            .size(10.dp, 6.dp)
                            .alpha(if (isSelected) 1f else 0.5f)
                            .then(
                                if (isSelected) {
                                    Modifier
                                } else {
                                    Modifier.alpha(0.5f)
                                }
                            )
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                    )
                }
            }

            // 底部输入区
            with(sharedTransitionScope) {
                AnimatedContent(windowHeightSizeClass) {
                    when (it) {
                        WindowHeightSizeClass.Compact -> {}
                        WindowHeightSizeClass.Medium, WindowHeightSizeClass.Expanded -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    onClick = goToAnalysis,
                                    shape = CardDefaults.shape,
                                    modifier = Modifier.animateContentSize()
                                ) {
                                    ASCardTextField(
                                        modifier = Modifier
                                            .then(
                                                if (windowsWidthSizeClass != WindowWidthSizeClass.Compact &&
                                                    pagerState.currentPage == 0
                                                ) Modifier.fillMaxWidth(0.8f)
                                                else Modifier.fillMaxWidth()
                                            )
                                            .sharedElement(
                                                sharedTransitionScope.rememberSharedContentState(
                                                    key = "card-input-analysis"
                                                ),
                                                animatedVisibilityScope = animatedContentScope
                                            ),
                                        value = "",
                                        onValueChange = {},
                                        enabled = false,
                                        readOnly = true,
                                        leadingIcon = {
                                            Icon(
                                                modifier = Modifier.sharedElement(
                                                    sharedTransitionScope.rememberSharedContentState(
                                                        key = "icon-input-analysis"
                                                    ),
                                                    animatedVisibilityScope = animatedContentScope
                                                ),
                                                imageVector = Icons.Outlined.Search,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    LoginInfoBottomDialog(popupUserInfoState, loginUserInfoState, userLoginPlatformList) {
        popupUserInfoState = false
    }
}


/**
 * 首页内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    vm: HomeViewModel,
    homeLayoutTypesetList: List<AppSettings.HomeLayoutItem>,
    downloadListState: List<AppDownloadTask>,
    goToDownloadPage: () -> Unit,
    goToPage: (NavKey) -> Unit
) {

    val bannerList by vm.bannerList.collectAsState()
    val bulletinInfo by vm.bulletinInfo.collectAsState()
    val appSettings by vm.appSettingsState.collectAsState()
    val appUpdateInfo by vm.appUpdateInfo.collectAsState()
    val useToolHistoryList by vm.useToolHistoryList.collectAsState()
    val context = LocalContext.current
    val uiState by vm.uiState.collectAsState()
    val windowsWidthSizeClass = rememberWidthSizeClass()
    val windowHeightSizeClass = rememberHeightSizeClass()
    val toolsHistoryCount = when (windowsWidthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium if windowHeightSizeClass != WindowHeightSizeClass.Compact -> 4
        WindowWidthSizeClass.Expanded if windowHeightSizeClass != WindowHeightSizeClass.Compact -> 6
        else -> 4
    }

    var closeBulletinDialogShow by remember { mutableStateOf(false) }
    var bulletinDialogShow by remember { mutableStateOf(false) }
    var unknownAppSign by remember { mutableStateOf(false) }

    val currentSHA1 = rememberSignatureSHA1(context)
    LaunchedEffect(currentSHA1) {
        if (currentSHA1 == null || !checkSign(currentSHA1)) {
            unknownAppSign = true
        }
    }

    LaunchedEffect(Unit) {
        vm.initOldAppInfo(context)
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 10.dp)
                .then(
                    if (windowsWidthSizeClass != WindowWidthSizeClass.Compact) Modifier.fillMaxWidth(
                        0.8f
                    ) else Modifier
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (unknownAppSign) {
                item {
                    ASWarringTip(
                        Modifier
                            .animateItem()
                            .animateContentSize()
                    ) {
                        Text(
                            if (BuildConfig.DEBUG) {
                                "当前App处于Debug模式，如果您并非开发人员，请谨慎使用，建议在Github公开的渠道进行下载。"
                            } else {
                                "当前应用签名未知，请谨慎使用！建议在Github公开的渠道进行下载。"
                            }
                        )
                    }
                }
            }

            homeLayoutTypesetList.forEach { layout ->
                if (!layout.isHidden) {
                    when (layout.type) {
                        AppSettings.HomeLayoutType.Banner if (!BuildConfig.ENABLED_PLAY_APP_MODE && bannerList.isNotEmpty()) -> {
                            item {
                                ASHorizontalCenteredHeroCarousel(
                                    autoScroll = true,
                                    modifier = Modifier
                                        .animateItem()
                                        .animateContentSize(),
                                    items = bannerList
                                ) { item ->
                                    Box(Modifier.maskClip(CardDefaults.shape)) {
                                        ASAsyncImage(
                                            model = item.url,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(168.dp)
                                                .maskClip(CardDefaults.shape),
                                            shape = CardDefaults.shape,
                                            onClick = {
                                                // 跳转链接
                                                context.openLink(item.ref)
                                            }
                                        )

                                        // 渐变黑色背景 + 文本
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.BottomStart)
                                                .background(
                                                    brush = Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Black.copy(alpha = 0.8f)
                                                        )
                                                    )
                                                )
                                        ) {
                                            Text(
                                                item.title,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.W500,
                                                color = Color.White,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        AppSettings.HomeLayoutType.Announcement -> {
                            if (appSettings.lastBulletinContent == bulletinInfo?.content) {
                                // 内容相同，不展示
                                return@forEach
                            }
                            item {
                                CommonInfoCard(
                                    modifier = Modifier
                                        .animateItem()
                                        .animateContentSize(),
                                    R.drawable.ic_brand_awareness_24px,
                                    "公告",
                                    bulletinInfo?.content ?: "暂无最新公告",
                                    onClickClose = {
                                        closeBulletinDialogShow = true
                                    },
                                    onClick = {
                                        if (bulletinInfo?.content.isNullOrEmpty()) return@CommonInfoCard
                                        bulletinDialogShow = true
                                    }
                                )
                            }
                        }

                        AppSettings.HomeLayoutType.UpdateInfo -> {

                            // Google Play 应用商店版本不展示更新内容
                            if (BuildConfig.ENABLED_PLAY_APP_MODE) return@forEach

                            if (getVersion(context).second == appSettings.lastSkipUpdateVersion) {
                                // 版本相同，不展示
                                return@forEach
                            }

                            // if (appUpdateInfo?.version == null) return@forEach
                            if (appUpdateInfo?.feat.isNullOrEmpty() || appUpdateInfo?.fix.isNullOrEmpty()) {
                                return@forEach
                            }

                            if (appUpdateInfo?.version == getVersion(context).second) {
                                return@forEach
                            }

                            item {
                                val content = when (getASBuildType(BuildConfig.FLAVOR)) {
                                    ASBuildType.OFFICIAL,
                                    ASBuildType.BETA -> {
                                        """
                                    新增：
                                    ${appUpdateInfo?.feat}
                                    修复：
                                    ${appUpdateInfo?.fix}
                                """.trimIndent()
                                    }

                                    ASBuildType.ALPHA -> appUpdateInfo?.feat
                                        ?: "Alpha版本请关注频道更新通知或GitHub Action构建。"
                                }
                                CommonInfoCard(
                                    modifier = Modifier
                                        .animateItem()
                                        .animateContentSize(),
                                    R.drawable.ic_info_24px,
                                    "更新内容",
                                    content,
                                    onClick = {
                                        context.openLink(appUpdateInfo?.url ?: "")
                                    }
                                )
                            }
                        }

                        AppSettings.HomeLayoutType.DownloadList -> {
                            item {
                                DownloadListCard(
                                    modifier = Modifier
                                        .animateItem()
                                        .animateContentSize(),
                                    downloadListState,
                                    goToDownloadPage = goToDownloadPage,
                                    onPauseTask = {
                                        vm.pauseDownloadTask(it.downloadSegment.segmentId)
                                    },
                                    onResumeTask = {
                                        vm.resumeDownloadTask(it.downloadSegment.segmentId)
                                    },
                                    onCancelTask = {
                                        vm.cancelDownloadTask(it.downloadSegment.segmentId)
                                    }
                                )
                            }
                        }

                        AppSettings.HomeLayoutType.Tools -> {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                ) {
                                    val allToolCount by remember(
                                        toolsHistoryCount,
                                        useToolHistoryList.size
                                    ) {
                                        mutableIntStateOf(
                                            min(
                                                toolsHistoryCount,
                                                useToolHistoryList.size
                                            )
                                        )
                                    }
                                    useToolHistoryList.take(allToolCount)
                                        .forEachIndexed { index, tool ->
                                            key(tool.title) {
                                                Surface(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(18.3f / 13f),
                                                    shape = CardDefaults.shape,
                                                    color = if ((index + 1) % 2 != 0) MaterialTheme.colorScheme.primaryContainer
                                                    else MaterialTheme.colorScheme.tertiaryContainer,
                                                    onClick = {
                                                        tool.let { vm.updateUseToolRecord(it) }
                                                        goToPage(tool.navKey)
                                                    }
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .padding(
                                                                horizontal = 16.dp,
                                                                vertical = 12.dp
                                                            )
                                                    ) {
                                                        tool.icon?.let {
                                                            Icon(
                                                                it,
                                                                contentDescription = "图标",
                                                                modifier = Modifier
                                                            )
                                                        } ?: run {
                                                            Icon(
                                                                painter = painterResource(tool.iconRes!!),
                                                                contentDescription = "图标",
                                                                modifier = Modifier
                                                            )
                                                        }
                                                        Spacer(Modifier.height(8.dp))
                                                        Text(tool.title, fontSize = 22.sp)
                                                        Spacer(Modifier.weight(1f))
                                                        Row(
                                                            Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.End
                                                        ) {
                                                            Icon(
                                                                Icons.AutoMirrored.Outlined.ArrowForward,
                                                                contentDescription = "前往"
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    repeat((toolsHistoryCount - allToolCount)) {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }


                            }
                        }

                        else -> {}
                    }
                }
            }

            item {
                Text(
                    "请在Download/BILIBILIAS目录下查看下载内容",
                    fontSize = 14.sp,
                    fontWeight = FontWeight(330),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }


    /**
     * 更新提示对话框
     */

    UpdateAppDialog(appUpdateInfo, uiState.shownAppUpdate, onAppUpdateDialogShown = {
        vm.onAppUpdateDialogShown()
    })

    /**
     * 关闭公告对话框
     */
    CloseBulletinDialog(closeBulletinDialogShow, onClickConfirm = {
        vm.updateLastBulletinContent()
    }, onClickDismiss = {
        closeBulletinDialogShow = false
    })

    /**
     * 公告对话框
     */
    BulletinDialog(bulletinInfo, bulletinDialogShow, onClickConfirm = {
        bulletinDialogShow = false
    })
}

@Composable
fun UpdateAppDialog(
    appUpdateInfo: AppUpdateConfigInfo?,
    shownAppUpdate: Boolean,
    onAppUpdateDialogShown: () -> Unit
) {
    var show by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (BuildConfig.ENABLED_PLAY_APP_MODE) return
    if (appUpdateInfo == null) return

    LaunchedEffect(Unit) {
        if (appUpdateInfo.version != getVersion(context).second && !shownAppUpdate) {
            show = true
            onAppUpdateDialogShown()
        }
    }
    ASAlertDialog(
        showState = show,
        title = { Text(stringResource(R.string.update_hint_title)) },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(stringResource(R.string.update_new_version_format, appUpdateInfo.version))
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.update_content))
                Spacer(Modifier.height(4.dp))
                Text(appUpdateInfo.feat)
            }
        },
        confirmButton = {
            ASTextButton(onClick = {
                context.openLink(appUpdateInfo.url)
            }) {
                Text(text = stringResource(R.string.update_go_download))
            }
        }, dismissButton = {
            ASTextButton(onClick = {
                show = false
            }) {
                Text(text = stringResource(R.string.common_cancel))
            }
        }, onDismiss = {
            show = false
        })
}

@Composable
private fun CloseBulletinDialog(
    show: Boolean, onClickConfirm: () -> Unit, onClickDismiss: () -> Unit
) {
    ASAlertDialog(
        showState = show,
        title = { Text(stringResource(R.string.home_close_announcement)) },
        text = {
            Text(stringResource(R.string.home_announcement_close_hint))
        },
        confirmButton = {
            ASTextButton(onClick = onClickConfirm) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            ASTextButton(onClick = onClickDismiss) {
                Text(text = "取消")
            }
        }
    )
}


@Composable
private fun BulletinDialog(
    bulletinConfigInfo: BulletinConfigInfo?,
    show: Boolean, onClickConfirm: () -> Unit,
) {
    ASAlertDialog(
        showState = show,
        title = { Text(stringResource(R.string.home_announcement)) },
        text = {
            Column(
                modifier = Modifier
            ) {
                Text(bulletinConfigInfo?.content ?: "")
                Text(bulletinConfigInfo?.publishDateTime ?: "")
            }
        },
        confirmButton = {
            ASTextButton(onClick = onClickConfirm) {
                Text(text = "确认")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DownloadListCard(
    modifier: Modifier = Modifier,
    downloadListState: List<AppDownloadTask>,
    goToDownloadPage: () -> Unit,
    onPauseTask: (task: AppDownloadTask) -> Unit,
    onResumeTask: (task: AppDownloadTask) -> Unit,
    onCancelTask: (task: AppDownloadTask) -> Unit,
) {
    SurfaceColorCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Download,
                    contentDescription = "下载列表图标",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .alpha(0.72f),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "下载列表",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.alpha(0.72f),
                )
                Spacer(Modifier.weight(1f))
                ASIconButton(onClick = {
                    goToDownloadPage.invoke()
                }, modifier = Modifier.size(30.dp)) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = "下载详情列表"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (downloadListState.isNotEmpty()) {
                    downloadListState.subList(0, min(3, downloadListState.size))
                        .forEach { task ->
                            DownloadTaskCard(task = task, onPause = {
                                onPauseTask(task)
                            }, onResume = {
                                onResumeTask(task)
                            }, onCancel = {
                                onCancelTask(task)
                            })
                        }
                } else {
                    Text(
                        "暂无缓存任务",
                        modifier = Modifier.alpha(0.72f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight(330),
                    )
                }
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalSharedTransitionApi::class
)
@Composable
private fun HomeScaffold(
    snackbarHostState: SnackbarHostState,
    loginUserInfoState: NetWorkResult<BILILoginUserModel?>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    goToLogin: () -> Unit,
    goToUserPage: () -> Unit,
    goToAnalysis: () -> Unit,
    goToSetting: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val haptics = LocalHapticFeedback.current
    val windowHeightSizeClass = rememberHeightSizeClass()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.width(15.dp))
                            Icon(
                                painterResource(R.drawable.ic_logo_mini),
                                contentDescription = stringResource(R.string.app_name),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .width(29.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = stringResource(R.string.app_name)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {},
                    actions = {
                        AsAutoError(
                            loginUserInfoState, onSuccessContent = {
                                ASAsyncImage(
                                    model = loginUserInfoState.data?.face,
                                    modifier = Modifier
                                        .size(40.dp),
                                    shape = CircleShape,
                                    contentDescription = "头像",
                                    onClick = {
                                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                                        goToUserPage()
                                    }
                                )
                            },
                            onLoadingContent = {
                                ContainedLoadingIndicator()
                            },
                            onDefaultContent = {
                                Row {
                                    ASIconButton(onClick = {
                                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                                        goToLogin()
                                    }) {
                                        Icon(
                                            Icons.Outlined.AccountCircle,
                                            contentDescription = "登录"
                                        )
                                    }
                                    Spacer(Modifier.width(2.dp))
                                    ASIconButton(onClick = goToSetting) {
                                        Icon(Icons.Outlined.Settings, contentDescription = "设置")
                                    }
                                }
                            },
                            onErrorContent = { _, _ ->
                                ASIconButton(onClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                                    goToLogin()
                                }) {
                                    Icon(
                                        Icons.Outlined.AccountCircle,
                                        contentDescription = "登录"
                                    )
                                }
                            })
                        Spacer(Modifier.width(15.dp))
                    }
                )
            }
        },
        floatingActionButton = {
            with(sharedTransitionScope) {
                AnimatedContent(windowHeightSizeClass) {
                    when (it) {
                        WindowHeightSizeClass.Compact -> {
                            FloatingActionButton(
                                onClick = goToAnalysis,
                                modifier = Modifier.sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(
                                        key = "card-input-analysis"
                                    ),
                                    animatedVisibilityScope = animatedContentScope,
                                ),
                                containerColor = MaterialTheme.colorScheme.primary,
                            ) {
                                Icon(
                                    modifier = Modifier.sharedElement(
                                        sharedTransitionScope.rememberSharedContentState(
                                            key = "icon-input-analysis"
                                        ),
                                        animatedVisibilityScope = animatedContentScope
                                    ),
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "视频解析",
                                )
                            }
                        }

                        WindowHeightSizeClass.Medium, WindowHeightSizeClass.Expanded -> {}
                    }
                }
            }
        }
    ) {
        content.invoke(it)
    }
}

@Composable
private fun CommonInfoCard(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    title: String = "",
    connect: String,
    onClickClose: (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    SurfaceColorCard(modifier = modifier) {
        Surface(Modifier.clickable {
            onClick.invoke()
        }, shape = CardDefaults.shape) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(iconId),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(0.72f),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        maxLines = 1,
                        modifier = Modifier.alpha(0.72f),
                    )
                    Spacer(Modifier.weight(1f))
                    if (onClickClose != null) {
                        ASIconButton(onClick = onClickClose, modifier = Modifier.size(30.dp)) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "关闭"
                            )
                        }
                    }
                }

                Text(
                    text = connect,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(330),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 16.dp)
                )

            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
private fun LoginInfoBottomDialog(
    popup: Boolean,
    loginUserInfoState: NetWorkResult<BILILoginUserModel?>,
    userLoginPlatformList: List<BILIUsersEntity>,
    onDismissRequest: () -> Unit,
) {
    if (popup) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(10.dp),

                ) {
                AsAutoError(loginUserInfoState, onSuccessContent = {
                    Column(
                        Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ASAsyncImage(
                            model = loginUserInfoState.data?.face,
                            contentDescription = "头像",
                            modifier = Modifier.size(100.dp),
                            shape = MaterialShapes.Square.toShape()
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(loginUserInfoState.data?.name ?: "", fontSize = 20.sp)
                        ASLoginPlatformFilterChipRow(userLoginPlatformList.map { it.loginPlatform })
                    }
                }, onLoadingContent = {
                    Column(
                        Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ContainedLoadingIndicator(Modifier.size(100.dp))
                        Text(stringResource(R.string.update_loading))
                    }
                })
            }
        }
    }
}

@Composable
private fun rememberSignatureSHA1(context: Context = LocalContext.current): String? {
    val packageName = context.packageName
    return remember(packageName, context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                packageName,
                if (android.os.Build.VERSION.SDK_INT >= 28)
                    PackageManager.GET_SIGNING_CERTIFICATES else PackageManager.GET_SIGNATURES
            )
            val signatures = if (android.os.Build.VERSION.SDK_INT >= 28) {
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }
            val cert = signatures?.getOrNull(0)?.toByteArray()
            if (cert != null) {
                val md = MessageDigest.getInstance("SHA1")
                val publicKey = md.digest(cert)
                publicKey.joinToString(":") { "%02X".format(it) }
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

private fun checkSign(actual: String?) : Boolean {
    val officeSign = "8E:B3:80:FB:C0:32:86:98:5B:8F:86:59:B2:79:16:75:A0:AB:21:DB"
    val officeAlphaSign = "7F:44:47:60:4B:BF:FB:A8:06:FD:13:DF:7F:E3:5D:AA:70:4B:D5:54"
    return isSignatureSHA1Match(actual, officeSign) ||
            isSignatureSHA1Match(actual, officeAlphaSign)
}

private fun isSignatureSHA1Match(actual: String?, expected: String): Boolean {
    return actual?.equals(expected, ignoreCase = true) == true
}