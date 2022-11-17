package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RestaurantService {
    @Headers("content-type: application/json")
    @GET("/v1/restaurants/simple")
    suspend fun getSearchRestaurantList(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Long?,
        @Query("size") size: Long?,
        @Query("sort") sort: Boolean?,
        @Query("name") name: String,
    ): Response<SimpleRestaurantResponse>
}