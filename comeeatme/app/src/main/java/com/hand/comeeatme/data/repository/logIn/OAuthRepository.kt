package com.hand.comeeatme.data.repository.logIn

import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.logIn.TokenResponse

interface OAuthRepository {
    suspend fun getToken(tokenRequest: TokenRequest): TokenResponse?
    suspend fun reissueToken(refreshToken: String): TokenResponse?
}