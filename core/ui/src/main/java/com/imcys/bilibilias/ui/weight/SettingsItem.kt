package com.imcys.bilibilias.ui.weight

import androidx.annotation.IntRange
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

// 摘抄
// https://github.com/SkyD666/Rays-Android/blob/master/app/src/main/java/com/skyd/rays/ui/component/SettingsItem.kt

val LocalUseColorfulIcon = compositionLocalOf { true }
val LocalVerticalPadding = compositionLocalOf { 16.dp }

@Composable
fun BannerItem(shape: Shape = RoundedCornerShape(36), content: @Composable () -> Unit) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        CompositionLocalProvider(
            LocalContentColor provides (LocalContentColor.current ),
            LocalVerticalPadding provides 21.dp
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(36))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                content()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SliderSettingsItem(
    imageVector: ImageVector?,
    text: String,
    value: Float,
    isImage: Boolean = false,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0)
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    valueFormat: String = "%.2f",
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit,
) {
    SliderSettingsItem(
        painter = imageVector?.let { rememberVectorPainter(image = it) },
        text = text,
        value = value,
        isImage = isImage,
        modifier = modifier,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        valueFormat = valueFormat,
        enabled = enabled,
        onValueChange = onValueChange,
    )
}

@Composable
fun SliderSettingsItem(
    painter: Painter?,
    text: String,
    value: Float,
    modifier: Modifier = Modifier,
    isImage: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0)
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    valueFormat: String = "%.2f",
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit,
) {
    BaseSettingsItem(
        painter = painter,
        text = text,
        modifier = modifier,
        enabled = enabled,
        isImage = isImage,
        description = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    modifier = Modifier.weight(1f),
                    value = value,
                    enabled = enabled,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChangeFinished = onValueChangeFinished,
                    onValueChange = onValueChange,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = String.format(valueFormat, value))
            }
        }
    )
}

@Composable
fun SwitchSettingsItem(
    imageVector: ImageVector?,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    checked: Boolean = false,
    enabled: Boolean = true,
    isImage: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    SwitchSettingsItem(
        painter = imageVector?.let { rememberVectorPainter(image = it) },
        text = text,
        modifier = modifier,
        description = description,
        checked = checked,
        enabled = enabled,
        isImage = isImage,
        onCheckedChange = onCheckedChange,
    )
}

@Composable
fun SwitchSettingsItem(
    painter: Painter?,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    checked: Boolean = false,
    enabled: Boolean = true,
    isImage: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    BaseSettingsItem(
        modifier = modifier.toggleable(
            value = checked,
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = { onCheckedChange?.invoke(it) },
        ),
        isImage = isImage,
        painter = painter,
        text = text,
        descriptionText = description,
        enabled = enabled,
    ) {
        ASCheckThumbSwitch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            interactionSource = interactionSource
        )
    }
}

@Composable
fun RadioSettingsItem(
    imageVector: ImageVector?,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    RadioSettingsItem(
        painter = imageVector?.let { rememberVectorPainter(image = it) },
        text = text,
        modifier = modifier,
        description = description,
        selected = selected,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun RadioSettingsItem(
    painter: Painter?,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    BaseSettingsItem(
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = { onClick?.invoke() },
            ),
        painter = painter,
        text = text,
        descriptionText = description,
        enabled = enabled,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource
        )
    }
}

@Composable
fun ColorSettingsItem(
    imageVector: ImageVector?,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    initColor: Color,
) {
    ColorSettingsItem(
        painter = imageVector?.let { rememberVectorPainter(image = it) },
        text = text,
        modifier = modifier,
        description = description,
        onClick = onClick,
        initColor = initColor,
    )
}

@Composable
fun ColorSettingsItem(
    painter: Painter?,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    initColor: Color,
) {
    BaseSettingsItem(
        painter = painter,
        text = text,
        modifier = modifier,
        descriptionText = description,
        onClick = onClick
    ) {
        ASIconButton(onClick = { onClick?.invoke() }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = initColor,
                        shape = RoundedCornerShape(50.dp)
                    )
            )
        }
    }
}

@Composable
fun BaseSettingsItem(
    modifier: Modifier = Modifier,
    painter: Painter?,
    text: String,
    descriptionText: String? = null,
    enabled: Boolean = true,
    isImage: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    dropdownMenu: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    BaseSettingsItem(
        modifier = modifier,
        painter = painter,
        text = text,
        description = if (descriptionText != null) {
            {
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else null,
        enabled = enabled,
        isImage = isImage,
        onClick = if (enabled) onClick else null,
        onLongClick = if (enabled) onLongClick else null,
        dropdownMenu = dropdownMenu,
        content = content,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseSettingsItem(
    modifier: Modifier = Modifier,
    painter: Painter?,
    text: String,
    description: (@Composable () -> Unit)? = null,
    isImage: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    dropdownMenu: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    CompositionLocalProvider(
        LocalContentColor provides if (enabled) {
            LocalContentColor.current
        } else {
            LocalContentColor.current.copy(alpha = 0.38f)
        },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .run {
                    if (onClick != null && enabled) {
                        combinedClickable(onLongClick = onLongClick) { onClick() }
                    } else this
                }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (painter != null) {
                if (isImage) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(30.dp),
                        painter = painter,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(24.dp),
                        painter = painter,
                        contentDescription = null,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp, vertical = LocalVerticalPadding.current)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                dropdownMenu?.invoke()
                if (description != null) {
                    Box(modifier = Modifier.padding(top = 5.dp)) {
                        description.invoke()
                    }
                }
            }
            content?.let {
                Box(modifier = Modifier.padding(end = 5.dp)) { it.invoke() }
            }
        }
    }
}

@Composable
fun CategorySettingsItem(text: String) {
    Text(
        modifier = Modifier.padding(
            start = 16.dp + 10.dp,
            end = 20.dp,
            top = 10.dp,
            bottom = 5.dp
        ),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun TipSettingsItem(text: String,modifier: Modifier? = null) {
    Column(
        modifier = modifier ?: Modifier.padding(horizontal = 16.dp + 10.dp, vertical = 10.dp),
    ) {
        Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}