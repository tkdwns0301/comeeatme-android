package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.logIn.TokenResponse
import retrofit2.Response
import retrofit2.http.*

interface OAuthService {
    // OAuth AccessToken을 통한 로그인
    @Headers("content-type: application/json")
    @POST("/login/oauth2/token/{registrationId}")
    suspend fun sendTokenToServer(
        @Path("registrationId") registrationId: String,
        @Body token: TokenRequest,
    ): Response<TokenResponse>

    // 토큰 재발급
    @Headers("content-type: application/json")
    @POST("/login/reissue")
    suspend fun reissueToken(
        @Header("Authorization") Authorization: String,
    ): Response<TokenResponse>
}