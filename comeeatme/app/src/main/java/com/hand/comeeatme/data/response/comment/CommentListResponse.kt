package com.hand.comeeatme.data.response.comment

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Member
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class CommentListResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: CommentListData,
)

data class CommentListData(
    @Expose
    val content: List<CommentListContent>,
    @Expose
    val pageable: Pageable,
    @Expose
    val sort: Sort,
    @Expose
    val numberOfElement: Long,
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

data class CommentListContent(
    @Expose
    val id: Long,
    @Expose
    val parentId: Long?,
    @Expose
    val deleted: Boolean,
    @Expose
    val content: String,
    @Expose
    val createdAt: String,
    @Expose
    val member: Member,
)
