package com.imcys.authentication.method

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.imcys.authentication.R
import com.imcys.designsystem.component.HyperlinkText
import com.imcys.designsystem.component.SingleLineText

@Composable
internal fun AuthMethodRoute(onNavigateToLoginAuth: () -> Unit, modifier: Modifier = Modifier) {
    AuthMethodScreen(onNavigateToLoginAuth, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthMethodScreen(onNavigateToLoginAuth: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_dialog_login_bottomsheet_title),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            AuthMethod(onNavigateToLoginAuth)
            // region 登录前的说明事项
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
            // endregion
        }
    }
}

@Composable
fun AuthMethod(onNavigateToLoginAuth: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth()) {
        AuthMethodItem(
            R.drawable.ic_launcher,
            R.string.app_dialog_login_bottomsheet_option_1,
            Modifier
                .weight(1f)
                .clickable(onClick = onNavigateToLoginAuth)
        )
        AuthMethodItem(
            R.drawable.ic_launcher,
            R.string.app_dialog_login_bottomsheet_option_2,
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthMethodItem(imageId: Int, loginDes: Int, modifier: Modifier) {
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
