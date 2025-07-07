package com.imcys.bilibilias.data.repository

import android.util.Log
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.network.utils.WebiTokenUtils
import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.BILIUserCookiesEntity
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.mapData
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.network.model.QRCodePollInfo
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import kotlinx.coroutines.flow.map

class QRCodeLoginRepository(
    private val webApiService: BILIBILIWebAPIService,
    private val tvAPIService: BILIBILITVAPIService,
    private val biliUsersDao: BILIUsersDao,
    private val biliUserCookiesDao: BILIUserCookiesDao
) {
    suspend fun getLoginQRCodeInfo(loginPlatform: LoginPlatform): FlowNetWorkResult<QRCodeInfo> {
        return when (loginPlatform) {
            LoginPlatform.WEB -> webApiService.qrcodeGenerate()
            LoginPlatform.MOBILE,
            LoginPlatform.TV -> tvAPIService.qrcodeGenerate().map { networkResult ->
                networkResult.mapData { tvQRCodeInfo, apiResponse ->
                    tvQRCodeInfo?.let {
                        QRCodeInfo(
                            qrcodeKey = it.authCode,
                            url = it.url
                        )
                    }
                }
            }
        }
    }

    /**
     * 获取扫码状态
     */
    suspend fun getQRScanState(
        loginPlatform: LoginPlatform,
        qrcodeKey: String
    ): FlowNetWorkResult<QRCodePollInfo> {
        return when (loginPlatform) {
            LoginPlatform.WEB -> webApiService.qrcodePoll(qrcodeKey).map { networkResult ->
                networkResult.mapData { tvQRCodeInfo, apiResponse ->
                    Log.d("networkResult", "getLoginQRCodeInfo: ${apiResponse?.responseHeader}")
                    tvQRCodeInfo
                }
            }

            LoginPlatform.MOBILE,
            LoginPlatform.TV -> tvAPIService.qrcodePoll(qrcodeKey).map { networkResult ->
                networkResult.mapData { tvQRCodePollInfo, apiResponse ->
                    tvQRCodePollInfo?.let {
                        QRCodePollInfo(
                            code = apiResponse?.code ?: 0,
                            message = apiResponse?.message ?: "",
                            refreshToken = it.refreshToken,
                            accessToken = it.accessToken,
                            url = "",
                            timestamp = it.expiresIn,
                            cookieInfo = it.cookieInfo
                        )
                    }
                }
            }
        }
    }


    suspend fun getLoginUserInfo(
        loginPlatform: LoginPlatform,
        accessKey: String? = null
    ): FlowNetWorkResult<BILILoginUserModel> {
        return when (loginPlatform) {
            LoginPlatform.WEB -> webApiService.getLoginUserInfo().map { networkResult ->
                networkResult.mapData { loginInfo, apiResponse ->
                    if (WebiTokenUtils.key == null) {
                        // 检测Webi
                        loginInfo?.wbiImg?.let { WebiTokenUtils.setKey(it) }
                    }
                    BILILoginUserModel(
                        face = loginInfo?.face,
                        level = loginInfo?.levelInfo?.currentLevel,
                        name = loginInfo?.uname,
                        mid = loginInfo?.mid
                    )
                }
            }

            LoginPlatform.MOBILE,
            LoginPlatform.TV -> tvAPIService.getLoginUserInfo(accessKey ?: "")
                .map { networkResult ->
                    networkResult.mapData { loginInfo, apiResponse ->
                        BILILoginUserModel(
                            face = loginInfo?.face,
                            mid = loginInfo?.mid,
                            level = loginInfo?.level,
                            name = loginInfo?.name
                        )
                    }
                }
        }

    }


    suspend fun saveLoginInfo(biliUsersEntity: BILIUsersEntity) =
        biliUsersDao.insertBILIUser(biliUsersEntity)

    suspend fun updateBILIUser(biliUsersEntity: BILIUsersEntity) =
        biliUsersDao.updateBILIUser(biliUsersEntity)

    suspend fun getBILIUserByMidAndPlatform(mid: Long, loginPlatform: LoginPlatform) =
        biliUsersDao.getBILIUserByMidAndPlatform(mid, loginPlatform)

    suspend fun deleteBILICookiesByUid(uid: Long) =
        biliUserCookiesDao.deleteBILICookiesByUid(uid)

    suspend fun insertBILIUserCookie(biliUserCookiesEntity: BILIUserCookiesEntity) =
        biliUserCookiesDao.insertBILIUserCookie(biliUserCookiesEntity)

    suspend fun getBILIUserListByUid(uid: Long) =
        biliUsersDao.getBILIUserListByUid(uid)
}