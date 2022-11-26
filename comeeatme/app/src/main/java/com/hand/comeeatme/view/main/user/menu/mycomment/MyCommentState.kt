package com.hand.comeeatme.view.main.user.menu.mycomment

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class MyCommentState {
    object Uninitialized : MyCommentState()
    object Loading : MyCommentState()
    object Success : MyCommentState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : MyCommentState()
}