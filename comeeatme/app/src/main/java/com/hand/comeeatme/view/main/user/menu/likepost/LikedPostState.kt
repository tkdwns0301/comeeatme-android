package com.hand.comeeatme.view.main.user.menu.likepost

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.PostResponse

sealed class LikedPostState {
    object Uninitialized: LikedPostState()
    object Loading: LikedPostState()
    data class Success(
        val response : PostResponse
    ): LikedPostState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): LikedPostState()
}