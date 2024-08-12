package com.imcys.bilibilias.feature.ffmpegaction

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultFfmpegActionComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : BaseViewModel<Unit, Unit>(componentContext),
    FfmpegActionComponent {

    @Composable
    override fun models(events: Flow<Unit>) = LoginPresenter()

    @AssistedFactory
    interface Factory : FfmpegActionComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultFfmpegActionComponent
    }
}

@Composable
private fun LoginPresenter(

) {

}
