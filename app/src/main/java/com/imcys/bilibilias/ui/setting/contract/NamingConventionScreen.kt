package com.imcys.bilibilias.ui.setting.contract


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.database.entity.download.FileNamePlaceholder
import com.imcys.bilibilias.database.entity.download.donghuaNamingRules
import com.imcys.bilibilias.database.entity.download.videoNamingRules
import com.imcys.bilibilias.datastore.AppSettingsSerializer
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.tip.ASInfoTip
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.maybeNestedScroll
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Serializable
data object NamingConventionRoute : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamingConventionScreen(
    namingConventionRoute: NamingConventionRoute,
    onToBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NamingConventionScaffold(
        scrollBehavior = scrollBehavior,
        onToBack
    ) { paddingValues ->
        NamingConventionContent(
            modifier = Modifier
                .maybeNestedScroll(scrollBehavior)
                .padding(paddingValues),
            onToBack = onToBack
        )
    }
}

@Composable
fun LazyItemScope.NamingRuleEditor(
    title: String,
    placeholderList: List<FileNamePlaceholder>,
    ruleValue: String,
    defaultRule: String,
    onRuleChange: (String) -> Unit,
    onRestoreDefault: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(ruleValue, selection = TextRange(ruleValue.length))) }
    LaunchedEffect(ruleValue) {
        if (ruleValue != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = ruleValue)
        }
    }

    Surface(
        shape = CardDefaults.shape,
        modifier = modifier
            .fillMaxWidth()
            .animateItem()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(title)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                placeholderList.forEach { item ->
                    AssistChip(onClick = {
                        onRuleChange(
                            if (ruleValue.isEmpty()) {
                                ruleValue + item.placeholder
                            } else {
                                ruleValue + "_" + item.placeholder
                            }
                        )
                        val newText = if (ruleValue.isEmpty()) {
                            ruleValue + item.placeholder
                        } else {
                            ruleValue + "_" + item.placeholder
                        }
                        // Move the cursor to the end after inserting a placeholder.
                        textFieldValue = TextFieldValue(newText, selection = TextRange(newText.length))
                    }, label = {
                        Text(
                            "${
                                item.placeholder.replace("{", "").replace("}", "")
                            }：${item.description}"
                        )
                    })
                }
            }
            AnimatedVisibility(ruleValue != defaultRule) {
                ASWarringTip {
                    Text(stringResource(R.string.app_view))
                }
            }
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    onRuleChange(it.text)
                },
                label = { Text(stringResource(R.string.setting_text_3)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!it.isFocused && ruleValue.isEmpty()) {
                            onRestoreDefault()
                        }
                    }
            )
            AnimatedVisibility(ruleValue != defaultRule) {
                Button(
                    shape = CardDefaults.shape,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRestoreDefault
                ) {
                    Text(stringResource(R.string.app_text_6))
                }
            }
        }
    }
}

@Composable
fun NamingConventionContent(
    modifier: Modifier = Modifier,
    onToBack: () -> Unit = {},
) {
    val vm = koinInject<NamingConventionViewModel>()
    var videoNamingRule by remember {
        mutableStateOf(AppSettingsSerializer.appSettingsDefault.videoNamingRule)
    }
    var donghuaNamingRule by remember {
        mutableStateOf(AppSettingsSerializer.appSettingsDefault.bangumiNamingRule)
    }

    LaunchedEffect(Unit) {
        vm.appSettings.collect {
            videoNamingRule = it.videoNamingRule
            donghuaNamingRule = it.bangumiNamingRule

            if (videoNamingRule.isEmpty()) {
                videoNamingRule = AppSettingsSerializer.appSettingsDefault.videoNamingRule
                vm.updateVideoNamingRule(videoNamingRule)
            }
            if (donghuaNamingRule.isEmpty()) {
                donghuaNamingRule = AppSettingsSerializer.appSettingsDefault.bangumiNamingRule
                vm.updateDonghuaNamingRule(donghuaNamingRule)
            }

        }
    }

    LazyColumn(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ASInfoTip {
                Text(
                    text = """
                            命名规则可以帮助你更好地管理下载的文件，你可以使用以下占位符来定义命名规则。
                            目前支持文件夹路径：也就是你可以做到 “{author}/{p_title}” 这样的命名规则，系统会自动创建对应的文件夹，无需处理文件类型。
                        """.trimIndent(),
                )
            }
        }
        item {
            ASWarringTip {
                Text(
                    text = """
                            当前命名规则仍在测试阶段，提供的变量较少，正在检验可靠程度，可能存在一些问题，如有任何问题，请前往社区反馈。
                            目前不支持对弹幕、字幕、封面等附加文件进行命名规则的设置。
                        """.trimIndent(),
                )
            }
        }
        item {
            NamingRuleEditor(
                title = stringResource(R.string.app_video_5),
                placeholderList = videoNamingRules,
                ruleValue = videoNamingRule,
                defaultRule = AppSettingsSerializer.appSettingsDefault.videoNamingRule,
                onRuleChange = {
                    vm.updateVideoNamingRule(it)
                },
                onRestoreDefault = {
                    vm.updateVideoNamingRule(AppSettingsSerializer.appSettingsDefault.videoNamingRule)
                }
            )
        }
        item {
            NamingRuleEditor(
                title = stringResource(R.string.app_bangumi),
                placeholderList = donghuaNamingRules,
                ruleValue = donghuaNamingRule,
                defaultRule = AppSettingsSerializer.appSettingsDefault.bangumiNamingRule,
                onRuleChange = {
                    vm.updateDonghuaNamingRule(it)
                },
                onRestoreDefault = {
                    vm.updateDonghuaNamingRule(AppSettingsSerializer.appSettingsDefault.bangumiNamingRule)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamingConventionScaffold(
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
                title = {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text("Beta")
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.setting_text_3))
                    }
                },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                },
                alwaysDisplay = false
            )
        },
    ) {
        content(it)
    }

}