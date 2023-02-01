package com.hand.comeeatme.data.response.member

import com.google.gson.annotations.Expose

data class MemberDuplicationResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: MemberDuplicationData,
)

data class MemberDuplicationData(
    @Expose
    val duplicate: Boolean,
)
