package com.imcys.bilibilias.core.designsystem.component

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.imcys.bilibilias.core.designsystem.theme.AsTheme

@ExperimentalMaterial3Api
@Composable
fun AsSingleChoiceSegmentedButtonRow(
    options: List<SegmentedButtonOption>,
    modifier: Modifier = Modifier,
    defaultSelectedItemIndex: Int = 0,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableIntStateOf(defaultSelectedItemIndex) }
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex.intValue = index; onItemSelection(selectedIndex.intValue)
                },
                selected = selectedIndex.intValue == index,
                enabled = option.enabled
            ) {
                Text(option.label)
            }
        }
    }
}

class SegmentedButtonOption(val label: String, val enabled: Boolean)

@Composable
fun rememberAsSingleChoiceSegmentedState() {
    LazyStaggeredGridState
    remember {
        AsSingleChoiceSegmentedState()
    }
}

class AsSingleChoiceSegmentedState {

}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Preview(showBackground = true)
@Composable
private fun PreviewRadioButton2() {
    AsTheme {
        val l = listOf(
            SegmentedButtonOption("AV1", true),
            SegmentedButtonOption("H265", true),
            SegmentedButtonOption("H264", false),
        )
        val index = remember {
            mutableIntStateOf(0)
        }
        AsSingleChoiceSegmentedButtonRow(l) {
            index.intValue = it
        }
    }
}