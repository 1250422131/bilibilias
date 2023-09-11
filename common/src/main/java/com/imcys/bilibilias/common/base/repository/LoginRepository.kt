package com.imcys.bilibilias.common.base.repository

import io.ktor.client.HttpClient
import javax.inject.Inject

class LoginRepository@Inject constructor(
    private val httpClient: HttpClient,
)
