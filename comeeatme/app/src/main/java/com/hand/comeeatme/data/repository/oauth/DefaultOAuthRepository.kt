package com.hand.comeeatme.data.repository.oauth

import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.like.SuccessResponse
import com.hand.comeeatme.data.response.logIn.TokenResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultOAuthRepository(
    private val oAuthService: OAuthService,
    private val ioDispatcher: CoroutineDispatcher,
) : OAuthRepository {
    override suspend fun getToken(
        tokenRequest: TokenRequest,
    ): TokenResponse? = withContext(ioDispatcher) {
        val response = oAuthService.sendTokenToServer(
            registrationId = "kakao",
            token = tokenRequest
        )

        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun reissueToken(
        refreshToken: String
    ): TokenResponse? = withContext(ioDispatcher) {
        val response = oAuthService.reissueToken(
            Authorization = "Bearer $refreshToken"
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun logout(accessToken: String): SuccessResponse? = withContext(ioDispatcher){
        val response = oAuthService.logout(
            Authorization = "Bearer $accessToken"
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }
}