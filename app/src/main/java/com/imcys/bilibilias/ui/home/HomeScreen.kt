package com.imcys.bilibilias.ui.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.R
import com.imcys.common.utils.getActivity
import com.imcys.bilibilias.common.base.components.IconCard
import com.zj.banner.BannerPager
import com.zj.banner.model.BaseBannerBean

@Composable
fun HomeScreen(
    goToNewVersionDoc: (Context) -> Unit,
    goToCommunity: (Context) -> Unit,
    goToDonateList: (Context) -> Unit,
    logoutLogin: () -> Unit,
) {
    Scaffold(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        topBar = {
            Text(
                stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        },
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
            val context = LocalContext.current
            IconCard(
                iconId = R.drawable.ic_tip_info,
                title = stringResource(R.string.app_fragment_home_update_content),
                longTitle = stringResource(id = R.string.app_fragment_home_version),
                doc = stringResource(R.string.new_version_doc),
                modifier = Modifier.clickable { goToNewVersionDoc(context) }
            )
            IconCard(
                iconId = R.drawable.ic_home_trophy,
                title = stringResource(R.string.app_fragment_home_salute),
                longTitle = stringResource(R.string.app_fragment_home_salute_text),
                doc = stringResource(R.string.app_fragment_home_salute_doc),
            )

            IconCard(
                iconId = R.drawable.ic_home_red_envelopes,
                title = stringResource(R.string.app_fragment_home_donate),
                longTitle = stringResource(R.string.app_fragment_home_donate_text),
                doc = stringResource(R.string.app_fragment_home_donate_doc)
            )
            IconCard(
                iconId = R.drawable.ic_home_rabbit,
                title = stringResource(id = R.string.app_fragment_home_feedback),
                longTitle = stringResource(R.string.app_fragment_home_feedback_text),
                doc = stringResource(id = R.string.app_fragment_home_feedback_doc),
                modifier = Modifier.clickable { goToCommunity(context) }
            )
            var show by remember { mutableStateOf(false) }
            IconCard(
                iconId = R.drawable.home_logout,
                title = stringResource(R.string.app_fragment_home_logout),
                longTitle = stringResource(R.string.app_fragment_home_logout_text),
                Modifier.clickable { show = true }
            )
            LogoutDialog(show = show, onDismiss = { show = false }, positiveButtonText = {
                logoutLogin()
                context.getActivity().finish()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

data class BannerBean(
    override val data: String
) : BaseBannerBean()

@Preview(showBackground = true)
@Composable
fun PreviewLogoutDialog() {
    LogoutDialog(true, { }, {})
}
