package com.hand.comeeatme.data.repository.restaurant

import com.hand.comeeatme.data.response.restaurant.DetailRestaurantResponse
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse

interface RestaurantRepository {
    suspend fun getSearchRestaurants(accessToken: String, page: Long?, size: Long?, sort: Boolean?, keyword: String): SimpleRestaurantResponse?
    suspend fun getRestaurantsRank(accessToken: String, page: Long?, size: Long?, addressCode: String, perImageNum: Long, sort: String): RestaurantsRankResponse?
    suspend fun getDetailRestaurant(accessToken: String, restaurantId: Long): DetailRestaurantResponse?
}