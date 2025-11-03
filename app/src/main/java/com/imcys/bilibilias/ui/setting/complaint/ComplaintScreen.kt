package com.imcys.bilibilias.ui.setting.complaint


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
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
                text = stringResource(R.string.complaint_up_zhu_quan_yi_bao_hu_ji),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            ComplaintOptionCard(
                title = stringResource(R.string.complaint_fang_an_yi__jin_zhi_huan),
                description = stringResource(R.string.complaint_bu_neng_rang_qi_ta_yong_h),
                buttonText = stringResource(R.string.complaint_cha_kan_xiang_qing),
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ComplaintOptionCard(
                title = stringResource(R.string.complaint_fang_an_er__zhui_hui_yi_h),
                description = stringResource(R.string.complaint_zhui_hui_yi_huan_cun_shi),
                buttonText = stringResource(R.string.complaint_cha_kan_xiang_qing),
                onClick = {  }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ComplaintOptionCard(
                title = stringResource(R.string.complaint_fang_an_san__jin_ji_xia_j),
                description = stringResource(R.string.complaint_li_ji_ting_zhi_bilibilias),
                buttonText = stringResource(R.string.complaint_cha_kan_xiang_qing),
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
                    Text(text = stringResource(R.string.complaint_tou_su))
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
