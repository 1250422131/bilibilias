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
import com.imcys.bilibilias.logic.login.QrCodeLoginAction
import com.imcys.bilibilias.logic.login.QrCodeLoginState
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    component: LoginComponent,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onBack: () -> Unit
) {
    val cookieLoginStateMachine = component.cookieStateMachine.produceStateMachine()
    val qrCodeStateMachine = component.qrCodeStateMachine.produceStateMachine()
    LoginContent(
        onBack = onBack,
        cookieLoginStateMachine = cookieLoginStateMachine,
        qrCodeStateMachine = qrCodeStateMachine,
        onShowSnackbar = onShowSnackbar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    onBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    cookieLoginStateMachine: FlowReduxStateMachine<State<CookieLoginState>, CookieAction>,
    qrCodeStateMachine: FlowReduxStateMachine<State<QrCodeLoginState>, QrCodeLoginAction>,
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
                        val state by remember { cookieLoginStateMachine.state }
                        CookieContent(
                            cookieLoginState = state,
                            dispatch = cookieLoginStateMachine.dispatchAction,
                            onBack = onBack,
                        )
                    }

                    1 -> {
                        val state by remember { qrCodeStateMachine.state }
                        QrContent(
                            state = state,
                            dispatch = qrCodeStateMachine.dispatchAction,
                            onShowSnackbar = onShowSnackbar,
                        )
                    }
                }
            }
        }
    }
}