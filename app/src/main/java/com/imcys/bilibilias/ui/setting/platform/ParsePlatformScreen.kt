package com.imcys.bilibilias.ui.setting.platform

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.data.repository.getDescription
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.TipSettingsItem
import com.imcys.bilibilias.ui.weight.tip.ASInfoTip
import com.imcys.bilibilias.weight.ASCommonLoadingScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
object ParsePlatformRoute : NavKey

@Composable
fun ParsePlatformScreen(parsePlatformRoute: ParsePlatformRoute, onToBack: () -> Unit) {
    ParsePlatformScreenScaffold(onToBack = onToBack) {
        ParsePlatformContent(Modifier.padding(it))
    }
}


@Composable
private fun ParsePlatformContent(modifier: Modifier = Modifier) {
    val vm = koinViewModel<ParsePlatformViewModel>()
    val uiState by vm.uiState.collectAsState()
    val useParsePlatform by vm.useParsePlatform.collectAsState()

    AnimatedContent(uiState) { state ->
        when (state) {
            is ParsePlatformViewModel.ParsePlatformUIState.AccountSelect -> {
                ParsePlatformAccountSelect(
                    modifier = modifier, state, onSelectAccount = vm::selectAccount
                )
            }

            ParsePlatformViewModel.ParsePlatformUIState.ChangeLoading -> {
                ASCommonLoadingScreen("正在切换账户中，请不要离开，避免登录失效。")
            }

            is ParsePlatformViewModel.ParsePlatformUIState.Default -> {
                ParsePlatformDefaultContent(
                    modifier = modifier, useParsePlatform,
                    onUpdateSelectPlatform = vm::updateSelectPlatform
                )
            }

            ParsePlatformViewModel.ParsePlatformUIState.EffectiveCheckLoading -> {
                ASCommonLoadingScreen("正在检查账户中，请不要离开，避免登录失效。")
            }
        }
    }
}

@Composable
private fun ParsePlatformAccountSelect(
    modifier: Modifier,
    uiState: ParsePlatformViewModel.ParsePlatformUIState.AccountSelect,
    onSelectAccount: (BILIUsersEntity) -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .selectableGroup()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ASInfoTip(Modifier.padding(horizontal = 10.dp)) {
            Text(
                """
                检测到你当前存在多个账户，请选择一个账户进行切换。
            """.trimIndent()
            )
        }
        uiState.accountList.forEach { user ->
            PlatformAccountCard(user, onClick = {
                onSelectAccount(user)
            })
        }
    }
}


@Composable
private fun PlatformAccountCard(user: BILIUsersEntity, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = CardDefaults.shape,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            ASAsyncImage(
                model = user.face,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "登录于 ${
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(user.createdAt)
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun ParsePlatformDefaultContent(
    modifier: Modifier,
    usePlatform: AppSettings.VideoParsePlatform,
    onUpdateSelectPlatform: (AppSettings.VideoParsePlatform) -> Unit
) {
    val parsePlatformList by remember(AppSettings.VideoParsePlatform.entries) {
        mutableStateOf(
            listOf(
                AppSettings.VideoParsePlatform.Web,
                AppSettings.VideoParsePlatform.TV
            )
        )
    }
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .selectableGroup()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        parsePlatformList.forEach { platform ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 10.dp)
                    .selectable(
                        selected = usePlatform == platform,
                        onClick = {
                            onUpdateSelectPlatform(platform)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = usePlatform == platform,
                    onClick = null
                )
                Text(
                    text = platform.getDescription(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        ParsePlatformDescription()
    }
}

@Composable
private fun ParsePlatformDescription() {
    TipSettingsItem(
        """
        不同平台解析到的内容和权益并不相同，比如TV平台部分内容和清晰度需要超级大会员，而非普通大会员。
        
        注意：TV平台和漫游功能不可同时使用，如果切换TV模式下漫游不会生效。
    """.trimIndent()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParsePlatformScreenScaffold(
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(text = "解析平台")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = {
                            onToBack.invoke()
                        })
                    },
                    alwaysDisplay = false
                )
            }
        },
    ) {
        content.invoke(it)
    }


}
