package com.imcys.network.repository.wbi

interface IWbiSignatureDataSources {
  suspend  fun getSignature(): String
}