package com.hand.comeeatme.data.response.member

import com.google.gson.annotations.Expose

data class MemberModifyProfileResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: MemberModifyProfileData,
)

data class MemberModifyProfileData(
    @Expose
    val id: Long,
)