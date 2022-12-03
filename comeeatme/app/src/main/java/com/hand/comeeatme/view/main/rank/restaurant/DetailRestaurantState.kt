package com.hand.comeeatme.view.main.rank.restaurant

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.image.RestaurantImageResponse
import com.hand.comeeatme.data.response.post.RestaurantPostResponse
import com.hand.comeeatme.data.response.restaurant.DetailRestaurantResponse

sealed class DetailRestaurantState {
    object Uninitialized : DetailRestaurantState()

    object Loading : DetailRestaurantState()

    data class Success(
        val response: DetailRestaurantResponse
    ) : DetailRestaurantState()

    data class ImageSuccess(
        val response: RestaurantImageResponse
    ): DetailRestaurantState()

    data class RestaurantPostsSuccess(
        val response: RestaurantPostResponse
    ): DetailRestaurantState()

    object FavoriteSuccess: DetailRestaurantState()
    object UnFavoriteSuccess: DetailRestaurantState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : DetailRestaurantState()
}