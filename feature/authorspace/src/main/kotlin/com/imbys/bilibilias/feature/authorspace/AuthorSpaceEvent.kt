package com.imbys.bilibilias.feature.authorspace

sealed interface AuthorSpaceEvent {
    data object NextPage : AuthorSpaceEvent
}