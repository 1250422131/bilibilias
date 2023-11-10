package com.imcys.model.cookie


import kotlinx.serialization.Serializable

@Serializable
data class AsCookie(
    val name: String,
    val value: String,
    val maxAge: Int = 0,
    val timestamp: Long,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
) {
    init {
        // DmSegMobileReq.ADAPTER
        // val d1 = DmSegMobileReq()
        // d1.from_scene
        // val grpcClient = GrpcClient.Builder()
        //     .client(OkHttpClient.Builder().protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE)).build())
        //     .baseUrl("serverUrl")
        //     .build()
        // val routeGuideClient = grpcClient.create(DMClient::class)
        // val dmSegMobile = routeGuideClient.DmSegMobile()
        // dmSegMobile.execute(d1)
        // GrpcClient.Builder().client().build()
    }
}
