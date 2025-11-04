package com.imcys.bilibilias.ui.setting.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.utils.ASConstant
import com.imcys.bilibilias.common.utils.openLink
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.maybeNestedScroll
import kotlinx.serialization.Serializable

@Serializable
data object AboutRouter : NavKey

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(aboutRouter: AboutRouter = AboutRouter, onToBack: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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
                title = { Text(text = stringResource(R.string.home_text_5679)) },
                navigationIcon = {
                    AsBackIconButton { onToBack.invoke() }
                },
                alwaysDisplay = false
            )
        },
    ) { paddingValues ->
        AboutContent(
            modifier = Modifier.maybeNestedScroll(scrollBehavior),
            paddingValues = paddingValues,
        )
    }

}

@Composable
fun AboutContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
) {
    LazyColumn(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            IconArea()
        }
        item {
            Spacer(Modifier.height(10.dp))
            TitleArea()
        }
        item {
            Spacer(Modifier.height(10.dp))
            ButtonArea()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonArea(
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Surface(
            shape = MaterialShapes.Cookie4Sided.toShape(),
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                context.openLink(ASConstant.QQ_CHANNEL_URL)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qq_channel_2px),
                contentDescription = stringResource(R.string.home_text_7870),
                modifier = Modifier
                    .padding(12.dp)
                    .size(28.dp),
            )
        }

        Surface(
            shape = MaterialShapes.Cookie4Sided.toShape(),
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                context.openLink(ASConstant.QQ_GROUP_URL)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qq_24px),
                contentDescription = stringResource(R.string.home_text_2566),
                modifier = Modifier
                    .padding(12.dp)
                    .size(28.dp),
            )
        }
    }
}

@Composable
fun TitleArea(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.home_text_4699),
            textAlign = TextAlign.Center,
            fontSize = 17.sp
        )

        Card(
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = """
                        过去，我们帮助了许多学生来缓存学习视频到其他设备离线观看，帮助了许多UP主来缓存剪辑视频素材。
                        
                        我们尊重每一位创作者的劳动成果，一路以来多处提醒用户禁止转载，也在积极探索更好的版权保护方案，希望能在尊重版权的前提下，帮助更多需要帮助的人。
                        请勿将本软件用于任何商业用途，一切后果自负。
                        
                        BILIBILIAS 自作者高中时期开始开发，至今已有数年时间，在此期间，作者不断学习和探索新的技术，至今已迭代到第三个重构版本。
                        非常感谢在这期间，所有给予我们反馈和帮助的用户和朋友们。
                    """.trimIndent(),
                )
            }
        }
    }
}

@Composable
fun IconArea() {
    Icon(
        modifier = Modifier
            .padding(4.dp)
            .size(60.dp),
        painter = painterResource(id = R.drawable.ic_logo_mini),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
    )
}
