package com.hand.comeeatme.view.main.home.post

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.comment.CommentListResponse
import com.hand.comeeatme.data.response.image.RestaurantImageResponse
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

    data class CommentListSuccess(
        val response: CommentListResponse?
    ): DetailPostState()

    data class RestaurantImageSuccess(
        val response: RestaurantImageResponse?
    ): DetailPostState()

    object WritingCommentSuccess: DetailPostState()
    object DeletePostSuccess: DetailPostState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): DetailPostState()

    object Loading: DetailPostState()
}