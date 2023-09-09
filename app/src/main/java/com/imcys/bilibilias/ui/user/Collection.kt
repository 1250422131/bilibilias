package com.imcys.bilibilias.ui.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.LocalNavController
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.common.base.components.IconArrowBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Collection() {
    val navHostController = LocalNavController.current
    FullScreenScaffold(Modifier.padding(horizontal = 20.dp), topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_activity_collection_title))
            },
            navigationIcon = {
                IconArrowBackButton {
                    navHostController.navigateUp()
                }
            }
        )
    }) {
    }
}
