package com.hand.comeeatme.view.main.user

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.PostResponse
import com.hand.comeeatme.data.response.user.UserDetailResponse

sealed class UserState {
    object Uninitialized : UserState()

    data class UserDetailSuccess(
        val response: UserDetailResponse,
    ) : UserState()

    data class UserPostSuccess(
        val response: PostResponse,
    ) : UserState()

    object LikePostSuccess : UserState()
    object UnLikePostSuccess : UserState()

    object BookmarkPostSuccess : UserState()
    object UnBookmarkPostSuccess : UserState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : UserState()

    object Loading : UserState()
}