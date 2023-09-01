package com.imcys.bilibilias.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.SplashRouter
import com.imcys.bilibilias.common.base.components.FullScreenScaffold

@Composable
fun AuthenticationMethodScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    FullScreenScaffold(modifier, topBar = {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_title),
                Modifier.height(40.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }) { innerPadding ->
        Column(Modifier.padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            AuthMethod(navController)
            Ability()
        }
    }
}

@Composable
fun AuthMethod(navController: NavHostController, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth()) {
        LoginMethodItem(
            R.mipmap.ic_launcher,
            R.string.app_dialog_login_bottomsheet_option_1,
            Modifier
                .weight(1f)
                .clickable { navController.navigate(SplashRouter.AuthScreen.route) }
        )
        LoginMethodItem(
            R.mipmap.ic_launcher,
            R.string.app_dialog_login_bottomsheet_option_2,
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun LoginMethodItem(imageId: Int, loginDes: Int, modifier: Modifier) {
    Column(
        modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = stringResource(loginDes),
            Modifier.size(70.dp)
        )
        Text(
            text = stringResource(loginDes),
            Modifier.padding(top = 10.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun Ability(modifier: Modifier = Modifier) {
    Column(
        modifier
            .padding(start = 30.dp, top = 20.dp, end = 30.dp, bottom = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_dialog_login_bottomsheet_text),
            fontWeight = FontWeight.Bold
        )
        Column(Modifier.padding(top = 20.dp)) {
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_list_text_1),
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_list_text_2),
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_list_text_3),
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_list_text_4),
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_list_text_5),
                maxLines = 1
            )
        }
    }
}
