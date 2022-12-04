package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.restaurant.DetailRestaurantResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantService {
    // 음식점 제목 및 주소 리스트 조회
    @GET("/v1/restaurants/simple")
    suspend fun getSearchRestaurantList(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Long?,
        @Query("size") size: Long?,
        @Query("sort") sort: Boolean?,
        @Query("keyword") keyword: String,
    ): Response<SimpleRestaurantResponse>

    // 음식점 리스트 조회 (좌표)

    // 음식점 상세 조회
    @GET("/v1/restaurants/{restaurantId}")
    suspend fun getDetailRestaurant(
        @Header("Authorization") Authorization: String,
        @Path("restaurantId") restaurantId: Long,
    ): Response<DetailRestaurantResponse>
}