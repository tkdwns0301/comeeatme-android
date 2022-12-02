package com.hand.comeeatme.data.request.comment

data class WritingCommentRequest(
    val parentId: Long?,
    val content: String,
)