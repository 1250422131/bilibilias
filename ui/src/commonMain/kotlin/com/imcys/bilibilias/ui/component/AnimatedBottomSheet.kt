package com.imcys.bilibilias.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.node.Ref
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
            onDismissRequest()
        }
    }
    // Make sure we dispose of the bottom sheet when it is no longer needed
    if (!sheetState.isVisible && !isVisible) {
        return
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AnimatedBottomSheetPreview() {
    AnimatedBottomSheet(
        isVisible = true,
        onDismissRequest = {}
    ) {
        Text("Sheet Content")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun <T> AnimatedBottomSheet(
    value: T?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.(T & Any) -> Unit,
) {
    LaunchedEffect(value != null) {
        if (value != null) {
            sheetState.show()
        } else {
            sheetState.hide()
            onDismissRequest()
        }
    }
    if (!sheetState.isVisible && value == null) {
        return
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
    ) {
        // Remember the last not null value: If our value becomes null and the sheet slides down,
        // we still need to show the last content during the exit animation.
        val notNullValue = lastNotNullValueOrNull(value) ?: return@ModalBottomSheet
        content(notNullValue)
    }
}

@Composable
fun <T> lastNotNullValueOrNull(value: T?): T? {
    val lastNotNullValueOrNullRef = remember { Ref<T>() }
    return value?.also {
        lastNotNullValueOrNullRef.value = it
    } ?: lastNotNullValueOrNullRef.value
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AnimatedBottomSheetGenericPreview() {
    data class SampleData(val text: String)

    val sampleValue = SampleData("Hello Preview")

    AnimatedBottomSheet(
        value = sampleValue,
        onDismissRequest = {}
    ) { data ->
        Text("Sheet Content: ${data.text}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InteractiveBottomSheetExample() {
    var currentSheetContent: SuperSpecialSheetContent by remember {
        mutableStateOf(SuperSpecialSheetContent.Simple("Simple Text"))
    }
    AnimatedBottomSheet(
        value = currentSheetContent,
        onDismissRequest = {},
    ) { sheetContent ->
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AnimatedContent(sheetContent) { specialSheetContent ->
                when (specialSheetContent) {
                    is SuperSpecialSheetContent.Simple ->
                        Text(specialSheetContent.title)

                    is SuperSpecialSheetContent.WithButton ->
                        Button(
                            onClick = {
                                currentSheetContent =
                                    SuperSpecialSheetContent.WithButton("Button Clicked")
                            }
                        ) {
                            Text(specialSheetContent.buttonText)
                        }
                }
            }
        }
    }
}

sealed interface SuperSpecialSheetContent {
    data class Simple(
        val title: String,
    ) : SuperSpecialSheetContent

    data class WithButton(
        val buttonText: String,
    ) : SuperSpecialSheetContent
}