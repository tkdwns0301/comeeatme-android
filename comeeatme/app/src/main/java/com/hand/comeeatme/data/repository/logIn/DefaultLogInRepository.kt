package com.hand.comeeatme.data.repository.logIn

import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.logIn.TokenResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultLogInRepository(
    private val oAuthService: OAuthService,
    private val ioDispatcher: CoroutineDispatcher,
) : LogInRepository {
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
}