package com.hand.comeeatme.data.response.member

import com.google.gson.annotations.Expose

data class MemberModifyResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: MemberModifyData
)

data class MemberModifyData(
    @Expose
    val id: Long,
)
