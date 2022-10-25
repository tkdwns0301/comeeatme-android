package com.hand.comeeatme.data

data class Request(
    val request: String,
)

// 로그인
data class LogInRequest(
    var accessToken: String,
)

