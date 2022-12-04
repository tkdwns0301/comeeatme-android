package com.hand.comeeatme.data.response.post

import com.google.gson.annotations.Expose

data class RestaurantPostResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: RestaurantPostData,
)

data class RestaurantPostData(
    @Expose
    val content: List<RestaurantPostContent>,
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

data class RestaurantPostContent(
    @Expose
    val id: Long,
    @Expose
    val imageUrls: List<String>,
    @Expose
    val content: String,
    @Expose
    val createAt: String,
    @Expose
    val member: Member,
    @Expose
    val liked: Boolean,
    @Expose
    val bookmarked: Boolean,
)
