package com.hand.comeeatme.data.response.user

import com.google.gson.annotations.Expose

data class UserInformationModifyResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: UserInformationModifyData
)

data class UserInformationModifyData(
    @Expose
    val id: Long,
)
