package com.hand.comeeatme.data.response.comment

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.ErrorResponse
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class MemberCommentsResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: MemberCommentsData,
    @Expose
    val error: ErrorResponse,
)

data class MemberCommentsData(
    @Expose
    val content: List<MemberCommentContent>,
    @Expose
    val pageable: Pageable,
    @Expose
    val sort: Sort,
    @Expose
    val numberOfElements: Long,
    @Expose
    val first: Boolean,
    @Expose
    val last: Boolean,
    @Expose
    val size: Long,
    @Expose
    val number: Long,
    @Expose
    val empty: Boolean,
)

data class MemberCommentContent(
    @Expose
    val id: Long,
    @Expose
    val content: String,
    @Expose
    val createdAt: String,
    @Expose
    val post: MemberCommentPost,
)

data class MemberCommentPost(
    val id: Long,
    val content: String,
    val imageUrl: String,
)