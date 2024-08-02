package com.base.hybridmvvm.data.model

data class SplashRequest(
    val deviceId: String
)

data class SplashResponse(
    val serverStatus: Boolean,
    val forceUpdate: Boolean,
    val updateUrl: String?
)
