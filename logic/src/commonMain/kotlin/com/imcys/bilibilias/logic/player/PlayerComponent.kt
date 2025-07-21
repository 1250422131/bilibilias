package com.imcys.bilibilias.logic.player

import com.arkivanov.decompose.ComponentContext

interface PlayerComponent {

}

class DefaultPlayerComponent(
    componentContext: ComponentContext
) : PlayerComponent, ComponentContext by componentContext {

}