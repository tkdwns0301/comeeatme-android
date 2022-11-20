package com.hand.comeeatme.view.main.home.post

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.DetailPostResponse

sealed class DetailPostState {
    object Uninitialized: DetailPostState()

    data class Success(
        val response: DetailPostResponse?,
    ) : DetailPostState()

    object LikePostSuccess: DetailPostState()
    object UnLikePostSuccess: DetailPostState()

    object BookmarkPostSuccess: DetailPostState()
    object UnBookmarkPostSuccess: DetailPostState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): DetailPostState()

    object Loading: DetailPostState()
}