package com.hand.comeeatme.data.request.user

data class UserModifyRequest(
    val nickname: String,
    val introduction: String?,
    val imageId: Long?,
)
