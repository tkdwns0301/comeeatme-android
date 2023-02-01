package com.hand.comeeatme.view.main.bookmark.post

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.bookmark.BookmarkPostContent

sealed class BookmarkPostState {
    object Uninitialized : BookmarkPostState()

    data class Success (
        val response: ArrayList<BookmarkPostContent>
            ): BookmarkPostState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): BookmarkPostState()

    object BookmarkPostSuccess: BookmarkPostState()

    object Loading: BookmarkPostState()
}