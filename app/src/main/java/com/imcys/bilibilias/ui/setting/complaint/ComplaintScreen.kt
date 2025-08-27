package com.imcys.bilibilias.ui.setting.complaint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import kotlinx.serialization.Serializable

@Serializable
object ComplaintRoute: NavKey

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintScreen(
    onToBack: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    ComplaintScaffold(scrollBehavior, onToBack = onToBack) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            Text(
                text = "UP主权益保护计划将继续实行，UP主或者其他主体可以按照类型投诉。",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            ComplaintOptionCard(
                title = "方案一：禁止缓存",
                description = "不能让其他用户在BILIBILIAS内缓存该视频。\n限制：只能是自己的视频。",
                buttonText = "查看详情",
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ComplaintOptionCard(
                title = "方案二：追回已缓存视频",
                description = "追回已缓存视频，向已缓存的用户发布邮件，要求在24小时内删除缓存的视频和副本。\n限制：只能是自己的视频或需要主体的身份认证。",
                buttonText = "查看详情",
                onClick = {  }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ComplaintOptionCard(
                title = "方案三：紧急下架与全网追回",
                description = "立即停止BILIBILIAS的一切活动，有必要时向所有用户发送追回邮件，要求42小时内删除缓存的视频和副本。\n限制：只能主体且需要可以代表主体的必要资料，BILIBILIAS所有渠道下架（Github和APP官网），APP所有版本阻止用户继续使用，并发布公告表明问题。",
                buttonText = "查看详情",
                onClick = {  }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComplaintScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = {
                    Text(text = "投诉")
                },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                }
            )
        },
    ) {
        content(it)
    }
}

@Composable
private fun ComplaintOptionCard(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = CardDefaults.shape) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(buttonText)
            }
        }
    }
}
