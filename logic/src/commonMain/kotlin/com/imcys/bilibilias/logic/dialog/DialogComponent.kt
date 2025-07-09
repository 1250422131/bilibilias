package com.imcys.bilibilias.logic.dialog

import com.arkivanov.decompose.ComponentContext

interface DialogComponent {
    val onDismissed: () -> Unit
}

class DefaultDialogComponent(
    componentContext: ComponentContext,
    override val onDismissed: () -> Unit
) : DialogComponent, ComponentContext by componentContext {

}