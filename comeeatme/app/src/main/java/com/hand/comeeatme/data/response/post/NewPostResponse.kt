package com.hand.comeeatme.data.response.post

import com.google.gson.annotations.Expose

data class NewPostResponse(
    @Expose
    val success: Boolean,
    val data: NewPostData,
)

data class NewPostData(
    @Expose
    val id: Long,
)
