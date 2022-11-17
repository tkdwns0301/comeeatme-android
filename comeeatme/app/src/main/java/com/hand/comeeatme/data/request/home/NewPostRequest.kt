package com.hand.comeeatme.data.request.home

data class NewPostRequest(
    var restaurantId: Long?,
    val hashtags: List<String>?,
    val imageIds: List<Long>?,
    val content: String?,
)
