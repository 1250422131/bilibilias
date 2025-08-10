package com.imcys.bilibilias.ui.setting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsMenuLink

@Composable
fun Section(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    onClick: () -> Unit,
) {
    Card(modifier = modifier.settingsSectionPadding()) {
        SettingsMenuLink(
            title = title,
            icon = icon,
            subtitle = subtitle,
            action = action,
            semanticProperties = semanticProperties,
            onClick = onClick,
        )
    }
}

fun Modifier.settingsSectionPadding(): Modifier = this.padding(8.dp)
