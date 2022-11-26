package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.logIn.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface OAuthService {
    @Headers("content-type: application/json")
    @POST("/login/oauth2/token/{registrationId}")
    suspend fun sendTokenToServer(
        @Path("registrationId") registrationId: String,
        @Body token: TokenRequest,
    ): Response<TokenResponse>
}