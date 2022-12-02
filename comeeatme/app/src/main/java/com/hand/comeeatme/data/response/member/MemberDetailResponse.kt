package com.hand.comeeatme.data.response.member

import com.google.gson.annotations.Expose

data class MemberDetailResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: MemberDetailData,
)

data class MemberDetailData(
    @Expose
    val id: Long,
    @Expose
    val nickname: String,
    @Expose
    val introduction: String,
    @Expose
    val imageUrl: String,
)
