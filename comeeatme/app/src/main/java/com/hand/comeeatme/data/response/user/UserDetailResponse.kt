package com.hand.comeeatme.data.response.user

import com.google.gson.annotations.Expose

data class UserDetailResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: UserDetailData,
)

data class UserDetailData(
    @Expose
    val id: Long,
    @Expose
    val nickname: String,
    @Expose
    val introduction: String,
    @Expose
    val imageUrl: String,
)
