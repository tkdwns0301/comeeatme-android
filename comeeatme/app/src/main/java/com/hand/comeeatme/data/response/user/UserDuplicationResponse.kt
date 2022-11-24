package com.hand.comeeatme.data.response.user

import com.google.gson.annotations.Expose

data class UserDuplicationResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: UserDuplicationData,
)

data class UserDuplicationData(
    @Expose
    val duplicate: Boolean,
)
