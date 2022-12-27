package com.hand.comeeatme.data.response.notice

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class NoticesResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: NoticesData,
)

data class NoticesData(
    @Expose
    val content: List<NoticesContent>,
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

data class NoticesContent(
    @Expose
    val type: String,
    @Expose
    val content: String,
    @Expose
    val createdAt: String,
)
