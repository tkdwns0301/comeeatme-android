package com.hand.comeeatme.view.main.user.menu.heartreview

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class HeartReviewState {
    object Uninitialized: HeartReviewState()
    object Loading: HeartReviewState()
    object Success: HeartReviewState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): HeartReviewState()
}