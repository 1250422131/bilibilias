package com.imcys.bilibilias.ui.analysis.components


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Preview
@Composable
fun SwitchSelectModelTabRow(isSelectSingleModel: Boolean = true, onSelectSingle: (Boolean) -> Unit = {}) {
    val haptics = LocalHapticFeedback.current
    var selectedTabIndex by remember(isSelectSingleModel) { mutableIntStateOf(
        if (isSelectSingleModel) 0 else 1
    ) }
    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        divider = {},
        modifier = Modifier
            .width(120.dp)
            .clip(CardDefaults.shape),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(selectedTabIndex, matchContentSize = false)
                    .fillMaxSize()
                    .clip(CardDefaults.shape)
            )
        }
    ) {
        Tab(
            selectedContentColor = MaterialTheme.colorScheme.onPrimary,
            unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selected = selectedTabIndex == 0, onClick = {
                if (selectedTabIndex != 0) {
                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                }
                onSelectSingle.invoke(true)
            }, modifier = Modifier.zIndex(2f)
        ) {
            Text(
                stringResource(R.string.app_single_select),
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                fontSize = 14.sp
            )
        }

        Tab(
            selectedContentColor = MaterialTheme.colorScheme.onPrimary,
            unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selected = selectedTabIndex == 1,
            onClick = {
                if (selectedTabIndex != 1) {
                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                }
                onSelectSingle.invoke(false)
            }, modifier = Modifier.zIndex(2f)
        ) {
            Text(
                stringResource(R.string.app_multi_select),
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                fontSize = 14.sp
            )
        }
    }
}