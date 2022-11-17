package com.hand.comeeatme.data.repository.logIn

import com.hand.comeeatme.data.request.logIn.TokenRequest
import com.hand.comeeatme.data.response.logIn.TokenResponse

interface LogInRepository {
    suspend fun getToken(tokenRequest: TokenRequest): TokenResponse?
}