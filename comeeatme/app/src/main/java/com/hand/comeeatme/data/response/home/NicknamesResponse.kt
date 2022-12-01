package com.hand.comeeatme.data.response.home

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class NicknamesResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: NicknameData,
)

data class NicknameData(
    @Expose
    val content: List<NicknameContent>,
    @Expose
    val pageable: Pageable,
    @Expose
    val sort: Sort,
    @Expose
    val numberOfElement: Int,
    @Expose
    val first: Boolean,
    @Expose
    val last: Boolean,
    @Expose
    val size: Int,
    @Expose
    val number: Int,
    @Expose
    val empty: Boolean,
)

data class NicknameContent(
    @Expose
    val id: Long,
    @Expose
    val nickname: String,
    @Expose
    val imageUrl: String,
)