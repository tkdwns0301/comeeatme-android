package com.hand.comeeatme.view.main.user.menu.myreview

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.PostResponse

sealed class MyReviewState {
    object Uninitialized: MyReviewState()

    object Loading: MyReviewState()

    data class Success(
        val response : PostResponse
    ): MyReviewState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): MyReviewState()
}