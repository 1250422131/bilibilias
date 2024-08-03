package com.imcys.network.download

sealed class ThirdPartyDownloader(val pkgName: String, val clsName: String) {
    data object ADMPro : ThirdPartyDownloader(ADM_PRO_PACK_NAME, ADM_PRO_EDITOR)
    data object ADM : ThirdPartyDownloader(ADM_PACK_NAME, ADM_EDITOR)
    data object IDMPlus : ThirdPartyDownloader(IDM_PLUS_PACK_NAME, IDM_DOWNLOADER)
    data object IDM : ThirdPartyDownloader(IDM_PACK_NAME, IDM_DOWNLOADER)
    data object NONE : ThirdPartyDownloader("", "")
}
