package com.hand.comeeatme.view.main.home

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.PostResponse

sealed class HomeState {
    object Uninitialized: HomeState()
    object Loading: HomeState()

    data class Success(
        val posts: PostResponse
    ): HomeState()

    object LikePostSuccess: HomeState()
    object UnLikePostSuccess: HomeState()

    object BookmarkPostSuccess: HomeState()
    object UnBookmarkPostSuccess: HomeState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): HomeState()

}