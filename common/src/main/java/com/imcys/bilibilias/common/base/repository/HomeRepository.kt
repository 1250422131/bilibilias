package com.imcys.bilibilias.common.base.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val httpClient: HttpClient)
