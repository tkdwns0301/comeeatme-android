package com.hand.comeeatme.service

import com.hand.comeeatme.data.LogInRequest
import com.hand.comeeatme.data.LogInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface OAuthService {
    @Headers("content-type: application/json")
    @POST("/login/oauth2/token/{registrationId}")
    fun sendTokenToServer(
        @Path("registrationId") registrationId: String,
        @Body token: LogInRequest,
    )
    : Call<LogInResponse>
}