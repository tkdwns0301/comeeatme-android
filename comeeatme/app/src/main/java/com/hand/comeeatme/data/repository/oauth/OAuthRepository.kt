package com.hand.comeeatme.data.repository.oauth

import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.like.SuccessResponse
import com.hand.comeeatme.data.response.logIn.TokenResponse

interface OAuthRepository {
    suspend fun getToken(tokenRequest: TokenRequest): TokenResponse?
    suspend fun reissueToken(refreshToken: String): TokenResponse?
    suspend fun logout(accessToken: String): SuccessResponse?
}