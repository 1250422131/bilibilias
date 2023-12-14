package com.imcys.space

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun CollectionDownloadRoute() {
    val viewModel: CollectionDownloadViewModel = hiltViewModel()
    val state by viewModel.collectionState.collectAsStateWithLifecycle()
    CollectionDownloadScreen(state, viewModel::changeQuality)
}

/** todo 顶部写一个全局 编码格式 选择器 */
fun codecs(codec: String): String {
    return if (codec.startsWith("av01"))
        "AV1"
    else if (codec.startsWith("avc1"))
        "AVC"
    else if (codec.startsWith("hev1"))
        "HEVC"
    else "UNKNOWN"
}

@Composable
fun CollectionDownloadScreen(state: ItemState, changeQuality: (String, Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            modifier = Modifier.weight(1f, false),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(state.items) { view ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(Modifier.padding(4.dp)) {
                        Text(text = view.videoDetails.title)
                        Row {
                            var selectedOptionText by remember {
                                mutableStateOf(view.playerPlayUrl.supportFormats.first().newDescription)
                            }
                            SimpleDropdownMenu(
                                items = view.playerPlayUrl.supportFormats,
                                value = selectedOptionText,
                                onValueChange = {
                                    selectedOptionText = it
                                },
                                onClick = {
                                    selectedOptionText = it.newDescription
                                    changeQuality(view.videoDetails.bvid, it.quality)
                                },
                            ) {
                                Text(text = it.newDescription)
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { /*开工了*/ },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
        ) {
            Text(text = "？？？")
        }
    }
}

@Composable
fun <T> SimpleDropdownMenu(
    items: List<T>,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    menuText: @Composable (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        SimpleTextField(
            modifier = Modifier
                .menuAnchor()
                .width(100.dp),
            readOnly = true,
            singleLine = true,
            value = value,
            onValueChange = onValueChange,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { menuText(selectionOption) },
                    onClick = { onClick(selectionOption);expanded = false },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholderText: String = "",
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        enabled = enabled,
        readOnly = readOnly,
        interactionSource = interactionSource,
        textStyle = textStyle.copy(fontSize = fontSize),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onTextLayout = onTextLayout,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        placeholderText,
                        style = textStyle
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}
