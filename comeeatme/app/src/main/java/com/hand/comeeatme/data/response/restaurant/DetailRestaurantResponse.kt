package com.hand.comeeatme.data.response.restaurant

import com.google.gson.annotations.Expose

data class DetailRestaurantResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: DetailRestaurantData
)

data class DetailRestaurantData(
    @Expose
    val id: Long,
    @Expose
    val name: String,
    @Expose
    val favoriteCount: Long,
    @Expose
    val hashtags: List<String>,
    @Expose
    val address: RestaurantAddress,
    @Expose
    val favorited: Boolean
)

data class RestaurantAddress(
    @Expose
    val name: String,
    @Expose
    val roadName: String
)
