package com.imcys.bilibilias.feature.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import com.imcys.bilibilias.core.network.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Composable
fun UserPresenter(
    events: Flow<Event>,
    userRepository: UserRepository,
    loginInfoDataSource: LoginInfoDataSource
): Model {
    var name by remember { mutableStateOf("") }
    var sign by remember { mutableStateOf("") }
    var faceUrl by remember { mutableStateOf("") }
    var fetchId: Int by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        val mid = loginInfoDataSource.mid.first()
        val card = userRepository.用户名片信息(mid)
        name = card.card.name
        sign = card.card.sign
        faceUrl = card.card.face
    }
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Event.SelectBreed -> fetchId++
                Event.FetchAgain -> fetchId++
            }
        }
    }
    return Model(name, sign, faceUrl)
}

sealed interface Event {
    data class SelectBreed(val breed: String) : Event
    object FetchAgain : Event
}

data class Model(
    val name: String,
    val sign: String,
    val faceUrl: String,
)