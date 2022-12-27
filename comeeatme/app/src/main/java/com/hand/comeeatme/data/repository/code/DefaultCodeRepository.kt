package com.hand.comeeatme.data.repository.code

import com.hand.comeeatme.data.network.CodeService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.request.code.AddressCodeResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultCodeRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
    private val codeService: CodeService,
    private val ioDispatcher: CoroutineDispatcher,
) : CodeRepository {
    override suspend fun getAddressCode(
        accessToken: String,
        parentCode: String?,
    ): AddressCodeResponse? = withContext(ioDispatcher) {
        val response = codeService.getAddressCode(
            "Bearer $accessToken",
            parentCode
        )

        if (response.isSuccessful) {
            response.body()!!
        }else if(response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if(response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getAddressCode(
                    "${appPreferenceManager.getAccessToken()}",
                    parentCode
                )
            } else {
                null
            }
        } else {
            null
        }
    }
}