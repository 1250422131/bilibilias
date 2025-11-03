package com.imcys.bilibilias.ui.setting.version


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.utils.DeviceInfoUtils
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.ASAnimatedContent
import com.imcys.bilibilias.weight.maybeNestedScroll
import kotlinx.serialization.Serializable


@Serializable
data object AppVersionInfoRoute : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppVersionInfoScreen(
    appVersionInfoRoute: AppVersionInfoRoute,
    onToBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    VersionInfoScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = onToBack
    ) { paddingValues ->
        VersionInfoContent(
            Modifier
                .padding(paddingValues)
                .maybeNestedScroll(scrollBehavior)
        )
    }
}

@Preview
@Composable
fun VersionInfoContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val deviceInfo by remember {
        mutableStateOf(DeviceInfoUtils.getDeviceInfo(context))
    }
    val copyText by remember {
        mutableStateOf(DeviceInfoUtils.getDeviceInfoCopyString(context))
    }
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        InfoRow(label = stringResource(R.string.version_version), value = deviceInfo.appVersion)
        InfoRow(label = stringResource(R.string.version_system_2), value = deviceInfo.systemVersion)
        InfoRow(label = stringResource(R.string.version_text_3), value = deviceInfo.model)
        InfoRow(label = stringResource(R.string.version_text_4), value = deviceInfo.marketModel)
        InfoRow(label = stringResource(R.string.version_text), value = deviceInfo.manufacturer)
        InfoRow(label = stringResource(R.string.version_text_2), value = deviceInfo.brandName)
        InfoRow(label = stringResource(R.string.version_system_1), value = deviceInfo.osName)
        InfoRow(label = stringResource(R.string.version_system), value = deviceInfo.osVersionName)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(stringResource(R.string.tools_version_info), copyText)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, stringResource(R.string.tools_copied_to_clipboard), Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.version_copy))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.version_text_1),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
        Text(
            text = value,
            modifier = Modifier.weight(2f),
            fontSize = 16.sp
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VersionInfoScaffold(
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
                title = { Text(text = stringResource(R.string.tools_version_info)) },
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