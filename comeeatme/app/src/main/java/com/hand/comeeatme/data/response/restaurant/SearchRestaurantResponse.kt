package com.hand.comeeatme.data.response.restaurant

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Sort

data class SimpleRestaurantResponse(
    @Expose
    val success: Boolean,
    val data: SimpleRestaurantData,
)

data class SimpleRestaurantData(
    @Expose
    val content: List<SimpleRestaurantContent>,
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
    val empty: Boolean,
)

data class SimpleRestaurantContent(
    @Expose
    val id: Long,
    @Expose
    val name: String,
    @Expose
    val addressName: String,
)
