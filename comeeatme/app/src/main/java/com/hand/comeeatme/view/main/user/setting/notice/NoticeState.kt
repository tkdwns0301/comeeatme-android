package com.hand.comeeatme.view.main.user.setting.notice

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.notice.NoticesContent

sealed class NoticeState {
    object Uninitialized: NoticeState()
    object Loading: NoticeState()

    data class Success(
        val response: ArrayList<NoticesContent>
    ): NoticeState()


    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): NoticeState()
}