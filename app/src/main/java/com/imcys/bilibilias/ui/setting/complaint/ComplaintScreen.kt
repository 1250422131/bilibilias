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
                text = stringResource(R.string.complaint_continue),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            ComplaintOptionCard(
                title = stringResource(R.string.complaint_cache),
                description = stringResource(R.string.complaint_yes),
                buttonText = stringResource(R.string.complaint_view_details),
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ComplaintOptionCard(
                title = stringResource(R.string.complaint_video),
                description = stringResource(R.string.complaint_delete_1),
                buttonText = stringResource(R.string.complaint_view_details),
                onClick = {  }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ComplaintOptionCard(
                title = stringResource(R.string.complaint_emergency_removal),
                description = stringResource(R.string.complaint_delete),
                buttonText = stringResource(R.string.complaint_view_details),
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
                    Text(text = stringResource(R.string.complaint_report))
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
