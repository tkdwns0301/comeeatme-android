package com.hand.comeeatme.data.repository.restaurant

import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse

interface RestaurantRepository {
    suspend fun getSearchRestaurants(accessToken: String, page: Long?, size: Long?, sort: Boolean?, keyword: String): SimpleRestaurantResponse?
}