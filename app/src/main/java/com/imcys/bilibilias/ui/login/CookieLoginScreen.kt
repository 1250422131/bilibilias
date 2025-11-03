package com.imcys.bilibilias.ui.login


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.ASAgreePrivacyPolicy
import com.imcys.bilibilias.weight.AsAutoError
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data object CookeLoginRoute : NavKey

@Composable
fun CookeLoginScreen(cookeLoginRoute: CookeLoginRoute, onToBack: () -> Unit, onFinish: () -> Unit) {
    val vm = koinViewModel<CookieLoginViewModel>()
    CookeLoginScaffold(onToBack) { paddingValues ->
        CookeLoginContent(vm, paddingValues, onFinish)
    }
}

@Composable
fun CookeLoginContent(
    vm: CookieLoginViewModel,
    paddingValues: PaddingValues,
    onFinish: () -> Unit
) {
    var cookiesString by remember { mutableStateOf("") }
    val userInfo by vm.loginUserInfoState.collectAsState()
    val scope = rememberCoroutineScope()
    var agreePrivacyPolicy by remember { mutableStateOf(false) }
    var showSavingDialog by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            enabled = agreePrivacyPolicy,
            modifier = Modifier.fillMaxWidth(),
            value = cookiesString,
            onValueChange = {
                cookiesString = it
                vm.checkCookies(it)
            },
            label = {Text(stringResource(R.string.login_copy))},
            maxLines = 4,
            minLines = 4
        )

        Spacer(Modifier.height(10.dp))

        ASAgreePrivacyPolicy(agreePrivacyPolicy, onClick = {
            agreePrivacyPolicy = !agreePrivacyPolicy
        })

        Spacer(Modifier.height(10.dp))
        // 用户卡片显示
        AsAutoError(
            userInfo,
            onDefaultContent = {},
            onSuccessContent = {
                if (!agreePrivacyPolicy) return@AsAutoError
                Column {
                    UserCard(
                        Modifier.shimmer(userInfo.status == ApiStatus.LOADING),
                        mid = userInfo.data?.mid ?: 0L,
                        name = userInfo.data?.name ?: "",
                        level = userInfo.data?.level ?: 0,
                        face = userInfo.data?.face ?: ""
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape, onClick = {
                            showSavingDialog = true
                            scope.launch {
                                vm.saveLoginCookie()
                                showSavingDialog = false
                                onFinish()
                            }
                        }) {
                        Text(stringResource(R.string.login_login_3))
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        )

        // 提示
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Text(stringResource(R.string.login_text_3))
        }

        SavingDialog(showSavingDialog)

    }


}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SavingDialog(show: Boolean) {
    ASAlertDialog(showState = show, title = {
        Text(stringResource(R.string.login_info))
    }, text = {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContainedLoadingIndicator()
            Text(text = stringResource(R.string.login_exit))
        }
    }, confirmButton = {})
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserCard(modifier: Modifier = Modifier, mid: Long, name: String, level: Int, face: String) {
    Surface(
        modifier = modifier.height(IntrinsicSize.Min), shape = CardDefaults.shape
    ) {
        Row(Modifier.padding(10.dp)) {
            Column(
                Modifier.weight(0.2f)

            ) {
                ASAsyncImage(
                    model = face,
                    shape = MaterialShapes.Cookie12Sided.toShape(),
                    contentDescription = stringResource(R.string.user_avatar_1),
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(0.8f)
            ) {
                Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("LV${level}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CookeLoginScaffold(onToBack: () -> Unit, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(stringResource(R.string.login_login))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        ASIconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.common_back)
                            )
                        }
                    }
                )
            }
        },
    ) {
        content.invoke(it)
    }
}
