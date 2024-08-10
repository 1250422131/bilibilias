package com.imcys.bilibilias.core.network.test

import JvmUnitTestDemoAssetManager
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DemoAsNetworkDataSource @Inject constructor(
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: TestAssetManager = JvmUnitTestDemoAssetManager,
) {
    companion object {
    }
}
