package com.hand.comeeatme.view.main.user.menu.myreview

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.Content

sealed class MyReviewState {
    object Uninitialized: MyReviewState()

    object Loading: MyReviewState()

    data class Success(
        val response : List<Content>
    ): MyReviewState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): MyReviewState()
}