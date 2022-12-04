package com.hand.comeeatme.data.response.image

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class RestaurantImageResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: RestaurantImageData,
)

data class RestaurantImageData(
    @Expose
    val content: List<RestaurantImageContent>,
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
    val number: Long,
    @Expose
    val empty: Boolean,
)

data class RestaurantImageContent(
    @Expose
    val restaurantId: Long,
    @Expose
    val postId: Long,
    @Expose
    val imageUrl: String,
)