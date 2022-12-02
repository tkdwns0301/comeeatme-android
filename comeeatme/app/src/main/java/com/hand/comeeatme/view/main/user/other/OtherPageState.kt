package com.hand.comeeatme.view.main.user.other

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.member.MemberDetailResponse
import com.hand.comeeatme.data.response.post.PostResponse

sealed class OtherPageState {
    object Uninitialized : OtherPageState()
    object Loading : OtherPageState()

    data class Success(
        val response: MemberDetailResponse,
    ) : OtherPageState()

    data class MemberPostSuccess(
        val response: PostResponse
    ): OtherPageState()

    object LikePostSuccess: OtherPageState()
    object UnLikePostSuccess: OtherPageState()
    object BookmarkPostSuccess: OtherPageState()
    object UnBookmarkPostSuccess: OtherPageState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : OtherPageState()
}