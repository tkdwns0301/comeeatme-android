package com.hand.comeeatme.data.response.comment

import com.google.gson.annotations.Expose

data class CommentResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: CommentData,
)

data class CommentData(
    @Expose
    val id: Long,
)