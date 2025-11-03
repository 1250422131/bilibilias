package com.imcys.bilibilias.ui.event.playvoucher


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import org.koin.compose.viewmodel.koinViewModel


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayVoucherErrorPage(onBlack:()-> Unit = {}) {
    val vm = koinViewModel<PlayVoucherErrorViewModel>()
    Scaffold(
        topBar = {
            ASTopAppBar(
                style = BILIBILIASTopAppBarStyle.Small,
                title = { Text(stringResource(R.string.error_feng_kong_xu_zhi)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                navigationIcon = {
                    ASIconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.common_fan_hui)
                        )
                    }
                },
                actions = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.error_zhang_hu_feng_xian_ti_shi),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                val points = listOf(
                    stringResource(R.string.error_nin_de_zhang_hao_ke_neng),
                    stringResource(R.string.error_jian_ce_dao_nin_de_tv_dua),
                    stringResource(R.string.error_gai_gong_neng_wei_lin_shi),
                    stringResource(R.string.error_ji_xu_shi_yong_ju_you_wei),
                    stringResource(R.string.error_wo_men_ke_neng_dui_yi_cha),
                    stringResource(R.string.error_qing_zai_cao_zuo_qian_cho)
                )
                items(points.size) { index ->
                    Text(
                        text = "• ${points[index]}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.error_dian_ji_xia_fang_an_niu_j),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Button(
                onClick = {
                    vm.ontUseTVVoucherInfo()
                    onBlack.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(stringResource(R.string.app_wo_yi_zhi_xiao))
            }
        }
    }
}