package com.imcys.bilibilias.logic.player

import com.imcys.bilibilias.logic.root.AppComponentContext

interface PlayerComponent {

}

class DefaultPlayerComponent(
    componentContext: AppComponentContext
) : PlayerComponent, AppComponentContext by componentContext {

}