package com.imcys.bilibilias.ui.setting.expand


import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.R
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.CategorySettingsItem
import com.imcys.bilibilias.ui.weight.SwitchSettingsItem


data object SystemExpandRoute : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemExpandScreen(systemExpandRoute: SystemExpandRoute, onToBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    SystemExpandScaffold(
        scrollBehavior = scrollBehavior,
        onToBack = onToBack
    ) { paddingValues ->
        SystemExpandContent(scrollBehavior, paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SystemExpandContent(
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        item {
            CategorySettingsItem(
                text = stringResource(R.string.app_permission_extension)
            )
        }

        item {
            SwitchSettingsItem(
                painter = painterResource(R.drawable.ic_shizuku_logo_512px),
                text = "Shizuku",
                description = stringResource(R.string.app_shizuku_unlock_features),
                checked = false,
                isImage = true
            ) { check ->

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SystemExpandScaffold(
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
                title = { Text(text = stringResource(R.string.app_extended_capabilities)) },
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