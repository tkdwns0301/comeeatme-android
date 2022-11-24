package com.hand.comeeatme.data.response.logIn

import com.google.gson.annotations.Expose

data class TokenResponse(
    @Expose
    var accessToken: String,
    @Expose
    var refreshToken: String,
    @Expose
    val memberId: Long,
)
