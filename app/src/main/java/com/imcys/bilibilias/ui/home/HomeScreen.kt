package com.imcys.bilibilias.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.weight.ASLoginPlatformFilterChipRow
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun HomeRoute(homeRoute: HomeRoute, goToLogin: () -> Unit, goToUserPage: () -> Unit) {
    HomeScreen(homeRoute, goToLogin, goToUserPage)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(homeRoute: HomeRoute, goToLogin: () -> Unit, goToUserPage: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val vm = koinViewModel<HomeViewModel>()
    val uiState by vm.uiState.collectAsState()
    val loginUserInfoState by vm.loginUserInfoState.collectAsState()
    val userLoginPlatformList by vm.userLoginPlatformList.collectAsState()
    var popupUserInfoState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.updateWebSpi()
    }

    LaunchedEffect(homeRoute.isFormLogin) {
        if (homeRoute.isFormLogin && !uiState.fromLoginEventConsumed) {
            vm.onNavigatedFromLogin()
            popupUserInfoState = true
        }
    }



    HomeScaffold(
        snackbarHostState = snackbarHostState,
        loginUserInfoState,
        goToLogin = goToLogin,
        goToUserPage = goToUserPage
    ) { p ->
        Column(Modifier.padding(p)) {
            LazyColumn(
                Modifier
                    .padding(horizontal = 15.dp)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 公告
                item {
                    CommonInfoCard(
                        R.drawable.ic_brand_awareness_24px,
                        "公告",
                        "这是一段公告内容"
                    )
                }
                item {
                    CommonInfoCard(
                        R.drawable.ic_info_24px,
                        "更新内容",
                        "这是一段更新内容"
                    )
                }

            }
        }
    }

    LoginInfoBottomDialog(popupUserInfoState, loginUserInfoState, userLoginPlatformList) {
        popupUserInfoState = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScaffold(
    snackbarHostState: SnackbarHostState,
    loginUserInfoState: NetWorkResult<BILILoginUserModel?>,
    goToLogin: () -> Unit,
    goToUserPage: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
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
                        Surface(
                            Modifier
                                .size(40.dp)
                                .clickable {
                                    if (loginUserInfoState.status == ApiStatus.SUCCESS) {
                                        goToUserPage()
                                    } else {
                                        goToLogin()
                                    }
                                },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            when (loginUserInfoState) {
                                is NetWorkResult.Success<*> -> AsyncImage(
                                    loginUserInfoState.data?.face,
                                    contentDescription = "头像",
                                    modifier = Modifier.fillMaxSize()
                                )

                                else -> {}
                            }
                        }
                        Spacer(Modifier.width(15.dp))
                    }
                )
            }
        }
    ) {
        content.invoke(it)
    }
}


@Composable
private fun CommonInfoCard(
    @DrawableRes iconId: Int,
    title: String = "",
    connect: String,
) {
    SurfaceColorCard {
        Surface(Modifier.clickable {}, shape = CardDefaults.shape) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(iconId),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(30.dp)
                            .alpha(0.72f),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.alpha(0.72f),
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {}, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = "关闭"
                        )
                    }

                }

                Text(
                    text = connect,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(330),
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (loginUserInfoState) {
                    is NetWorkResult.Success<*> -> {
                        Surface(
                            shape = MaterialShapes.Square.toShape()
                        ) {
                            AsyncImage(
                                loginUserInfoState.data?.face,
                                contentDescription = "头像",
                                modifier = Modifier.size(100.dp),
                            )
                        }
                        Spacer(Modifier.height(5.dp))
                        Text(loginUserInfoState.data?.name ?: "", fontSize = 20.sp)
                        ASLoginPlatformFilterChipRow(userLoginPlatformList.map { it.loginPlatform })
                    }

                    is NetWorkResult.Loading<*> -> {
                        ContainedLoadingIndicator(Modifier.size(100.dp))
                        Text("正在加载...")
                    }

                    else -> {}
                }
            }
        }
    }
}