package com.hand.comeeatme.data.response.user

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class UserSearchResultResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: UserSearchResultData
)

data class UserSearchResultData(
    @Expose
    val content: List<UserSearchResultContent>,
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

data class UserSearchResultContent(
    @Expose
    val id: Long,
    @Expose
    val nickname: String,
    @Expose
    val imageUrl: List<String>,
)
