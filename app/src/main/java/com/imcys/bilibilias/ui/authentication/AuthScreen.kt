package com.imcys.bilibilias.ui.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.SplashRouter
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.components.FullScreenScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AuthViewModel>()
    FullScreenScaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                stringResource(R.string.app_dialog_login_qr_bottomsheet_title),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        })
    }) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(BilibiliApi.getLoginQRPath)
                    .listener(
                        onStart = {},
                        onCancel = {},
                        onError = { request, result ->

                        },
                    ) { request, result ->

                    }
                    .build(),
                contentDescription = "qr code",
                contentScale = ContentScale.Inside,
                modifier = Modifier.size(130.dp)
            )
            Text(
                text = "未扫码",
                Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Button(
                onClick = { navController.navigate(SplashRouter.Screen.route) },
                Modifier
                    .padding(horizontal = 25.dp, vertical = 10.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.app_dialog_login_qr_bottomsheet_finish),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { },
                Modifier
                    .padding(horizontal = 25.dp, vertical = 10.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(R.string.app_dialog_login_qr_bottomsheet_download),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { },
                Modifier
                    .padding(horizontal = 25.dp, vertical = 10.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(R.string.app_dialog_login_qr_bottomsheet_go),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
