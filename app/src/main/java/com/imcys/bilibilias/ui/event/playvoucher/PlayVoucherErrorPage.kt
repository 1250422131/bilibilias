package com.imcys.bilibilias.ui.event.playvoucher

import androidx.compose.foundation.background
import androidx.compose.ui.res.stringResource
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
                title = { Text(stringResource(R.string.home_text_570)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                navigationIcon = {
                    ASIconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.back)
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
                        text = stringResource(R.string.account_risk_warning),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                val points = listOf(
                    stringResource(R.string.home_risk_control_violation_third_party),
                    stringResource(R.string.home_verification_abnormal_identity),
                    stringResource(R.string.home_temporary_auxiliary),
                    stringResource(R.string.home_violation_third_party_tool),
                    stringResource(R.string.home_abnormal_freeze_mark),
                    stringResource(R.string.home_risk_consequence_bear)
                )
                items(points.size) { index ->
                    Text(
                        text = "• ${points[index]}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.click_button_below_notice),
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
                Text(stringResource(R.string.acknowledged))
            }
        }
    }
}