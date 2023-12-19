package com.imcys.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imcys.common.utils.getActivity
import com.imcys.common.utils.openUri
import com.zj.banner.BannerPager

@Composable
internal fun HomeScreen(
    logout: () -> Unit,
    navigationToDonation: () -> Unit,
    navigateToContribute: () -> Unit,
) {
    Scaffold(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val items = remember {
                arrayListOf(
                    BannerBean("https://www.wanandroid.com/blogimgs/8a0131ac-05b7-4b6c-a8d0-f438678834ba.png"),
                    BannerBean("https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png"),
                    BannerBean("https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png"),
                    BannerBean("https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png"),
                )
            }
            BannerPager(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                items = items
            ) { item ->
            }
            UpdateContent()

            Contribute(navigateToContribute)

            // 捐款
            Donation(navigationToDonation)

            FeedbackProblem()

            Logout(logout)
        }
    }
}

@Composable
private fun Contribute(navigateToContribute: () -> Unit) {
    Card(
        onClick = navigateToContribute,
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        ListItem(
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_trophy),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            },
            headlineContent = {
                Text(
                    stringResource(R.string.app_fragment_home_salute_text),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(R.string.app_fragment_home_salute_doc))
            },
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        )
    }
}

@Composable
private fun Donation(navigationToDonation: () -> Unit) {
    Card(
        onClick = navigationToDonation,
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        ListItem(
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_red_envelopes),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            },
            headlineContent = {
                Text(
                    stringResource(R.string.app_fragment_home_donate_text),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(R.string.app_fragment_home_donate_doc))
            },
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        )
    }
}

@Composable
private fun Logout(logoutLogin: () -> Unit) {
    var show by remember { mutableStateOf(false) }
    Card(
        onClick = { show = true },
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        ListItem(
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.home_logout),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            },
            headlineContent = {
                Text(
                    stringResource(R.string.app_fragment_home_logout),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(R.string.app_fragment_home_logout_text))
            },
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        )
    }
    val context = LocalContext.current
    LogoutDialog(
        show = show,
        onDismiss = { show = false },
        positiveButtonText = {
            logoutLogin()
            context.getActivity().finish()
        }
    )
}

/** 社区反馈问题 */
@Composable
private fun FeedbackProblem() {
    val context = LocalContext.current
    Card(
        onClick = {
            val uri = Uri.parse("https://support.qq.com/product/337496")
            context.openUri(uri)
        },
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        ListItem(
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_rabbit),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            },
            headlineContent = {
                Text(
                    stringResource(R.string.app_fragment_home_feedback),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(R.string.app_fragment_home_feedback_doc))
            },
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        )
    }
}

@Composable
private fun UpdateContent() {
    val context = LocalContext.current
    Card(
        onClick = {
            val uri = Uri.parse("https://docs.qq.com/doc/DVXZNWUVFakxEQ2Va")
            context.openUri(uri)
        },
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        ListItem(
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_tip_info),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            },
            headlineContent = {
                Text(
                    stringResource(R.string.app_fragment_home_update_content),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(R.string.new_version_doc))
            },
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        )
    }
}

@Composable
fun LogoutDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    positiveButtonText: () -> Unit,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = positiveButtonText) {
                    Text("是的")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("点错了")
                }
            },
            title = {
                Text("确定要退出登录了吗？")
            }
        )
    }
}
