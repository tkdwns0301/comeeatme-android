package com.hand.comeeatme.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Response(
    val response: String,
)

// 로그인
@Parcelize
data class LogInResponse(
    var accessToken: String,
    var refreshToken: String,
    val memberId: Number,
) : Parcelable