package com.imcys.network.utils

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface INetworkMonitor {
    val isOnline: Flow<Boolean>
}