package com.hand.comeeatme.data.repository.favorite

import com.hand.comeeatme.data.response.favorite.FavoritePostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse

interface FavoriteRepository {
    suspend fun favoriteRestaurant(accessToken: String, restaurantId: Long): SuccessResponse?
    suspend fun unFavoriteRestaurant(accessToken: String, restaurantId: Long): SuccessResponse?
    suspend fun getAllFavorite(
        accessToken: String,
        memberId: Long,
        page: Long?,
        size: Long?,
        sort: Boolean?,
    ): FavoritePostResponse?
}