package com.hand.comeeatme.view.main.bookmark.favorite

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.favorite.FavoritePostResponse

sealed class FavoriteRestaurantState {
    object Uninitialized : FavoriteRestaurantState()

    data class Success (
        val response: FavoritePostResponse
    ): FavoriteRestaurantState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): FavoriteRestaurantState()

    object FavoritePostSuccess: FavoriteRestaurantState()

    object Loading: FavoriteRestaurantState()
}