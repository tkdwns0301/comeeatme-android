package com.hand.comeeatme.view.main.user.edit

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class UserEditState {
    object Uninitialized : UserEditState()

    object Success : UserEditState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : UserEditState()

    data class CompressPhotoFinish (
        val compressPhotoList: ArrayList<String>,
    ) : UserEditState()

    object Loading : UserEditState()

}