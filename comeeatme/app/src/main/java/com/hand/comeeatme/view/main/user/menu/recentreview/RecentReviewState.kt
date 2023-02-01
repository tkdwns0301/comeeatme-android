package com.hand.comeeatme.view.main.user.menu.recentreview

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class RecentReviewState {
    object Uninitialized: RecentReviewState()
    object Loading: RecentReviewState()
    object Success: RecentReviewState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): RecentReviewState()
}