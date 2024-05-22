package com.imcys.bilibilias.feature.home

sealed interface HomeEvent {
    data object Logout : HomeEvent

}