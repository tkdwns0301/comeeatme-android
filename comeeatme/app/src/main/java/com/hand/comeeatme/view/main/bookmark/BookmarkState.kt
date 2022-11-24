package com.hand.comeeatme.view.main.bookmark

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class BookmarkState {
    object Uninitialized: BookmarkState()

    object Success: BookmarkState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message : String
    ): BookmarkState()

    object Loading: BookmarkState()
}