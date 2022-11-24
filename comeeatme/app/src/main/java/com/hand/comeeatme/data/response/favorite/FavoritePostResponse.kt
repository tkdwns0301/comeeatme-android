package com.hand.comeeatme.data.response.favorite

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class FavoritePostResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: FavoritePostData,
)

data class FavoritePostData(
    @Expose
    val content: List<FavoritePostContent>,
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

data class FavoritePostContent(
    @Expose
    val id: Long,
    @Expose
    val name: String,
    @Expose
    val favorited: Boolean,
)
