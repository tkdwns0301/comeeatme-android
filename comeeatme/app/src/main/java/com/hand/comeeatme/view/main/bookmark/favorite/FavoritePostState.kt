package com.hand.comeeatme.view.main.bookmark.favorite

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.favorite.FavoritePostResponse

sealed class FavoritePostState {
    object Uninitialized : FavoritePostState()

    data class Success (
        val response: FavoritePostResponse
    ): FavoritePostState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): FavoritePostState()

    object FavoritePostSuccess: FavoritePostState()

    object Loading: FavoritePostState()
}