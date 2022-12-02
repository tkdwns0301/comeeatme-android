package com.hand.comeeatme.data.response.member

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class MemberSearchResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: MemberSearchData
)

data class MemberSearchData(
    @Expose
    val content: List<MemberSearchContent>,
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

data class MemberSearchContent(
    @Expose
    val id: Long,
    @Expose
    val nickname: String,
    @Expose
    val imageUrl: String,
)
