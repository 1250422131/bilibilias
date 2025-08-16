package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freeletics.flowredux2.FlowReduxStateMachine
import com.freeletics.flowredux2.produceStateMachine
import com.imcys.bilibilias.logic.login.CookieAction
import com.imcys.bilibilias.logic.login.CookieLoginState
import com.imcys.bilibilias.logic.login.LoginComponent
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    component: LoginComponent,
    onBack: () -> Unit
) {
    val cookieLoginStateMachine = component.cookieStateMachine.produceStateMachine()
    LoginContent(
        onBack = onBack,
        cookieLoginStateMachine = cookieLoginStateMachine
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    onBack: () -> Unit,
    cookieLoginStateMachine: FlowReduxStateMachine<State<CookieLoginState>, CookieAction>,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("登录", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val pagerState = rememberPagerState { TabItem.valueOf().size }
            val scope = rememberCoroutineScope()
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                TabItem.valueOf().forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            scope.launch {
                                selectedTabIndex = index
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = {
                            Icon(
                                title.icon,
                                contentDescription = title.title,
                            )
                        },
                        text = {
                            Text(text = title.title, fontSize = 14.sp)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                when (it) {
                    0 -> {
                        val state by cookieLoginStateMachine.state
                        CookieContent(
                            cookieLoginState = state,
                            dispatch = cookieLoginStateMachine.dispatchAction,
                        )
                    }

                    1 -> LoginByQr(
                        url = "",
                        timeLeftInSeconds = 0,
                        qrStatusMessage = "",
                        authCodeUrl = "",
                        onRefreshQr = {},
                        onSaveQr = {},
                        onQrCodeScanned = {},
                        dispatch = {}
                    )
                }
            }
        }
    }
}

// Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("扫码登录") },
//                navigationIcon = { BackButton(onBack = onBack) }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            Modifier.padding(innerPadding).fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            val painter = rememberQrCodePainter(data = url)
//            Spacer(modifier = Modifier.height(30.dp))
//            Image(
//                painter = painter,
//                contentDescription = null,
//                modifier = Modifier.size(150.dp).clickable(null, null) {
//                    dispatch(LoginAction.RequestNewQrCode)
//                }
//            )
//            val kmpContext = LocalKmpContext.current
//            if (kmpContext.platform == Platform.ANDROID) {
//                val scope = rememberCoroutineScope()
//                Button(
//                    onClick = {
//                        scope.launch {
//                            savePainterToGallery(
//                                kmpContext,
//                                painter,
//                                Clock.System.now().toEpochMilliseconds().toString()
//                            )
//                        }
//                    }
//                ) {
//                    Text("保存二维码")
//                }
//            }
//        }
//    }