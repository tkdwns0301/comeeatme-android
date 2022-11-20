package com.hand.comeeatme.data.response.home

import com.google.gson.annotations.Expose
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
    val pageable: String,
    val sort: Sort,
    val numberOfElement: Int,
    val first: Boolean,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val empty: Boolean,
)

data class NicknameContent(
    @Expose
    val id: Long,
    val nickname: String,
    val imageUrl: String,
)