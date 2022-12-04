package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.favorite.FavoritePostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse
import retrofit2.Response
import retrofit2.http.*

interface FavoriteService {
    // 음식점 맛집 즐겨찾기 그룹 지정 X
    @PUT("/v1/member/favorite/{restaurantId}")
    suspend fun favoriteRestaurant(
        @Header("Authorization") Authorization: String,
        @Path("restaurantId") restaurantId: Long,
    ): Response<SuccessResponse>

    // 음식점 맛집 즐겨찾기 취소 그룹 지정 X
    @DELETE("/v1/member/favorite/{restaurantId}")
    suspend fun unFavoriteRestaurant(
        @Header("Authorization") Authorization: String,
        @Path("restaurantId") restaurantId: Long,
    ): Response<SuccessResponse>

    // 즐겨찾기된 음식점 조회
    @GET("/v1/members/{memberId}/favorite")
    suspend fun getAllFavorite(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
        @Query("page") page: Long,
        @Query("size") size: Long?,
        @Query("sort") sort: Boolean?,
    ): Response<FavoritePostResponse>

}