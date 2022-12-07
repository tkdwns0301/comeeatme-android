package com.hand.comeeatme.data.response.restaurant

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class RestaurantsRankResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: RestaurantsRankData,
)

data class RestaurantsRankData(
    @Expose
    val content: List<RestaurantsRankContent>,
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

data class RestaurantsRankContent(
    @Expose
    val id: Long,
    @Expose
    val name: String,
    @Expose
    val postCount: Long,
    @Expose
    val favoriteCount: Long,
    @Expose
    val address: RestaurantAddress,
    @Expose
    val favorited: Boolean,
    @Expose
    val hashtags: List<String>,
    @Expose
    val imageUrls: List<String>,
)
