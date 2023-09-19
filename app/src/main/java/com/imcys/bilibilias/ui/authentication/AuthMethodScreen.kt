package com.imcys.bilibilias.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.SplashRouter
import com.imcys.bilibilias.common.base.components.CenterRow
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.common.base.components.HyperlinkText
import com.imcys.bilibilias.common.base.components.SingleLineText

@Composable
fun AuthMethodScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    FullScreenScaffold(modifier, topBar = {
        CenterRow(Modifier.height(40.dp)) {
            Text(
                text = stringResource(R.string.app_dialog_login_bottomsheet_title),
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
                .clickable {
                    navController.navigate(SplashRouter.AuthScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
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

@OptIn(ExperimentalTextApi::class)
@Composable
fun Ability(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.app_dialog_login_bottomsheet_text),
            fontWeight = FontWeight.Bold
        )
        Text(
            buildAnnotatedString {
                appendLine(stringResource(R.string.app_dialog_login_bottomsheet_list_text_1))
                appendLine(stringResource(R.string.app_dialog_login_bottomsheet_list_text_2))
                appendLine(stringResource(R.string.app_dialog_login_bottomsheet_list_text_3))
                appendLine(stringResource(R.string.app_dialog_login_bottomsheet_list_text_4))
                appendLine(stringResource(R.string.app_dialog_login_bottomsheet_list_text_5))
            },
            Modifier.padding(start = 20.dp),
            textAlign = TextAlign.Start
        )
        Text(
            stringResource(R.string.app_dialog_login_bottomsheet_text2),
            Modifier.padding(top = 30.dp),
            fontWeight = FontWeight.Bold
        )
    }
    // todo 放到 common 常量文件
    val link1 = stringResource(R.string.app_dialog_login_bottomsheet_list_text2_1)
    HyperlinkText(
        link1,
        mapOf(link1 to "https://www.bilibili.com/blackboard/topic/activity-cn8bxPLzz.html")
    )
    val link2 = stringResource(R.string.app_dialog_login_bottomsheet_list_text2_2)
    HyperlinkText(
        link2,
        mapOf(
            link2 to "https://docs.qq.com/doc/p/080e6bdd303d1b274e7802246de47bd7cc28eeb7?dver=2.1.27292865"
        )
    )
    SingleLineText(
        stringResource(R.string.app_dialog_login_bottomsheet_list_text2_3),
        color = MaterialTheme.colorScheme.primary,
    )
    SingleLineText(
        stringResource(R.string.app_dialog_login_bottomsheet_list_text2_4),
        color = MaterialTheme.colorScheme.primary
    )
    SingleLineText(
        stringResource(R.string.app_dialog_login_bottomsheet_list_text2_5),
        color = MaterialTheme.colorScheme.primary
    )
}
