package com.imcys.bilibilias.ui.setting.roam

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.BannerItem
import com.imcys.bilibilias.ui.weight.TipSettingsItem


@Composable
@Preview
fun PreviewRoamScreen() {
    RoamScreen({})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoamScreen(onToBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    RoamSettingScaffold(
        scrollBehavior = scrollBehavior,
        onToBack
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues)) {
            item {
                BannerItem {
                    Row(Modifier.padding(vertical = 5.dp, horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("启用漫游", fontSize = 18.sp)
                        Spacer(Modifier.weight(1f))
                        Switch(
                            checked = false,
                            onCheckedChange = {

                            }
                        )
                    }
                }
            }

            item {
                TipSettingsItem("""
                    漫游服务并非会使用VPN方式提供代理，而是中间服务器白名单请求转发。
                    
                    开启后您的账户的部分网络请求和身份信息会被带到我们的服务器上，通过我们的服务代理请求。
                """.trimIndent())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoamSettingScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = { Text(text = "漫游服务") },
                navigationIcon = {
                    IconButton(onClick = {
                        onToBack.invoke()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "返回",
                        )
                    }
                }
            )
        },
    ) {
        content(it)
    }

}