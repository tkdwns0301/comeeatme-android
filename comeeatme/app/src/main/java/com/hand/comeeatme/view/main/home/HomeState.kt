package com.hand.comeeatme.view.main.home

import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.home.PostResponse

sealed class HomeState {
    object Uninitialized: HomeState()
    object Loading: HomeState()

    data class Success(
        val posts: PostResponse
    ): HomeState()

    data class Error(
        @StringRes val message: String
    ): HomeState()

}