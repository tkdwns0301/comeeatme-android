package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.favorite.FavoritePostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse
import retrofit2.Response
import retrofit2.http.*

interface FavoriteService {
    @PUT("/v1/member/favorite/{restaurantId}")
    suspend fun favoritePost(
        @Header("Authorization") Authorization: String,
        @Path("restaurantId") restaurantId: Long,
    ): Response<SuccessResponse>

    @DELETE("/v1/member/favorite/{restaurantId}")
    suspend fun unFavoritePost(
        @Header("Authorization") Authorization: String,
        @Path("restaurantId") restaurantId: Long,
    ): Response<SuccessResponse>

    @GET("/v1/members/{memberId}/favorite")
    suspend fun getAllFavorite(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
        @Query("page") page: Long,
        @Query("size") size: Long?,
        @Query("sort") sort: Boolean?,
    ): Response<FavoritePostResponse>

}