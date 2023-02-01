package com.hand.comeeatme.view.main.user.menu.mycomment

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.comment.MemberCommentContent

sealed class MyCommentState {
    object Uninitialized : MyCommentState()
    object Loading : MyCommentState()
    data class Success(
        val response: ArrayList<MemberCommentContent>
    ) : MyCommentState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : MyCommentState()
}