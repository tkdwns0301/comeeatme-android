package com.hand.comeeatme.data.request.post

data class ModifyPostRequest(
    val restaurantId: Long,
    val hashtags: List<String>,
    val content: String,
)
